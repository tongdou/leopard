package io.github.leopard.core.strategy.track;

import com.alibaba.fastjson.JSON;
import io.github.leopard.common.constant.CacheConstants;
import io.github.leopard.common.constant.CurrencyConstants;
import io.github.leopard.common.utils.BigDecimalUtil;
import io.github.leopard.common.utils.CacheUtils;
import io.github.leopard.core.strategy.StrategyParam;
import io.github.leopard.core.strategy.exception.StrategyExecuteException;
import io.github.leopard.core.strategy.impl.BandTrackingStrategySupport;
import io.github.leopard.core.strategy.model.TrackTransactionParamDTO;
import io.github.leopard.core.strategy.model.TrackTransactionDTO;
import io.github.leopard.exchange.client.IExchangeApi;
import io.github.leopard.exchange.extension.GateApiExtension;
import io.github.leopard.exchange.model.dto.request.EatSpotOrderMarketRequestDTO;
import io.github.leopard.exchange.model.dto.result.EatSpotOrderMarketResultDTO;
import io.github.leopard.exchange.model.dto.result.SpotAccountResultDTO;
import io.github.leopard.exchange.model.dto.result.TickResultDTO;
import io.github.leopard.exchange.model.enums.CandlesticksIntervalEnum;
import io.github.leopard.exchange.model.enums.SideEnum;
import io.github.leopard.system.service.DictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 条件筛选交易市场追踪买卖
 *
 * @author meteor
 */
@Component("choiceHigherTrackStrategy")
public class ChoiceHigherTransactionStrategy extends AbstractTrackCandlestickStrategy<TrackTransactionParamDTO, TrackTransactionDTO, EatSpotOrderMarketResultDTO> {

    @Autowired
    private DictService dictService;
    @Override
    public void execute(IExchangeApi exchangeApi, StrategyParam<String, String> param) throws StrategyExecuteException {
        super.execute(exchangeApi, param);
    }

    /**
     * 构造监控参数
     *
     * @param param
     * @return
     */
    @Override
    protected TrackTransactionParamDTO buildMonitoringParam(StrategyParam<String, String> param) {
        return JSON.parseObject(JSON.toJSONString(param), TrackTransactionParamDTO.class);
    }

    /**
     * 计算需要查询的币种
     *
     * @param api
     * @param monitoringParam
     * @return
     */
    @Override
    protected List<String> fetchCurrencyPriceList(GateApiExtension api, TrackTransactionParamDTO monitoringParam) {
        //排名
        Integer limit_ranking = StringUtils.isBlank(monitoringParam.getLimit_ranking()) ? null : Integer.parseInt(monitoringParam.getLimit_ranking());
        //总交易量
        BigDecimal quote_volume = StringUtils.isBlank(monitoringParam.getQuote_volume()) ? null : new BigDecimal(monitoringParam.getQuote_volume());
        //过滤的交易对
        List<String> filterMarket = monitoringParam.getFilter_market();

        List<TickResultDTO> tickResultDTOS = api.fetchRankingTickerList(limit_ranking, quote_volume, filterMarket);
        List<String> currencyList = tickResultDTOS.stream().map(TickResultDTO::getCurrencyPair).collect(Collectors.toList());
        return currencyList;
    }

    /**
     * 计算查询结果
     *
     * @param monitoringParam
     * @param market          市场币种
     * @return
     */
    @Override
    protected TrackTransactionDTO computeMonitoringResult(TrackTransactionParamDTO monitoringParam, String market) {
        GateApiExtension client = GateApiExtension.create();
        BigDecimal changePercentage = client.fetchCurrencyTodayChangePercentage(market);
        //预期的涨幅
        BigDecimal expectChangePercentage = new BigDecimal(monitoringParam.getUpward_percent());
        //符合预期
        if (changePercentage.compareTo(expectChangePercentage) >= 0) {
            TrackTransactionDTO trackTransactionDTO = new TrackTransactionDTO();
            trackTransactionDTO.setBuy(Boolean.TRUE);
            trackTransactionDTO.setMarket(market);
            trackTransactionDTO.setPurchaseAmount(new BigDecimal(monitoringParam.purchase_amount));
            trackTransactionDTO.setRetreatRatio(new BigDecimal(monitoringParam.getRetreat_ratio()));
            return trackTransactionDTO;
        }
        return null;
    }


    /**
     * 特殊操作
     *
     * @param monitoringParam
     * @param dataResult
     * @param api
     * @return
     */
    @Override
    protected Boolean specialProcess(TrackTransactionParamDTO monitoringParam, TrackTransactionDTO dataResult, GateApiExtension api) {
        // 计算结果不允许购买
        if (!dataResult.getBuy()) {
            return Boolean.FALSE;
        }
        //判断账户是否有usdt,没用的话就不交易了
        SpotAccountResultDTO spotAccountResultDTO = api.spotAccountMust(CurrencyConstants.USDT);
        //账户金额不够,不交易
        if (spotAccountResultDTO.getAvailable().compareTo(dataResult.getPurchaseAmount()) < 0) {
            return Boolean.FALSE;
        }
        String market = dataResult.getMarket();

        //已经买过的不需要再买了
        if (!Objects.isNull(CacheUtils.get(CacheConstants.MARKET_BUYED + market))) {
            return Boolean.FALSE;
        }
        //BTC跌到下限 不能交易
        String btcDownWardPercent = dictService.getLabel("leopard_global_param", "btc_down_ward_percent");
        BigDecimal todayChangePercentage = api.fetchCurrencyTodayChangePercentage(CurrencyConstants.BTC_USDT);
        if(StringUtils.isNotBlank(btcDownWardPercent)&& todayChangePercentage.compareTo(new BigDecimal(btcDownWardPercent))<0){
            return Boolean.FALSE;
        }
        return true;
    }


    /**
     * 交易流程
     *
     * @param trackTransaction
     * @param api
     * @return
     */
    @Override
    protected EatSpotOrderMarketResultDTO transactionProcess(TrackTransactionDTO trackTransaction, GateApiExtension api) {
        String market = trackTransaction.getMarket();
        EatSpotOrderMarketRequestDTO request = new EatSpotOrderMarketRequestDTO();
        request.setSideEnum(SideEnum.BUY);
        request.setMarket(market);
        request.setUsdtAmt(trackTransaction.getPurchaseAmount());
        //下单成功
        EatSpotOrderMarketResultDTO orderMarketResultDTO = api.eatSpotOrderMarketMustOrNull(request);
        //已经下单的,存入缓存
        CacheUtils.put(CacheConstants.MARKET_BUYED+market,market);
        //委托卖单
        new BandTrackingStrategySupport(api).syncExecute(
                market,
                orderMarketResultDTO.getCost(),
                orderMarketResultDTO.getTokenNumber(),
                BigDecimalUtil.roundingHalfUp(trackTransaction.getRetreatRatio().divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP)),
                CandlesticksIntervalEnum.M_5);
        return orderMarketResultDTO;
    }


    @Override
    protected String buildWxMessage(TrackTransactionDTO dataResult, EatSpotOrderMarketResultDTO transaction) {
        StringBuffer message = new StringBuffer();
        //交易对
        String currency = dataResult.getMarket();
        //订单id
        String orderId = transaction.getOrderId();
        //买入价格
        String price = transaction.getPrice().toString();
        //成交数量
        String tokenNumber = transaction.getTokenNumber().stripTrailingZeros().toPlainString();
        //设置标题
        message.append("<font size=6>追踪策略购买成功</font> \n ");
        message.append("<font size=6 color=red > [").append(currency).append("]</font> \n ");
        message.append("买入价格：").append(price).append("\n");
        message.append("成交数量：").append(tokenNumber).append("\n");
        message.append("订单ID：").append(orderId).append("\n");
        //换行
        message.append("请密切关注行情走势,数据来源【gate.io】\n");
        return message.toString();
    }
}
