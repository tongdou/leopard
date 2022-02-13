package io.github.leopard.core.strategy.track;

import com.alibaba.fastjson.JSON;
import io.gate.gateapi.models.SpotAccount;
import io.gate.gateapi.models.SpotPricePutOrder;
import io.gate.gateapi.models.Ticker;
import io.github.leopard.common.constant.CurrencyConstants;
import io.github.leopard.common.utils.StringUtils;
import io.github.leopard.core.strategy.StrategyParam;
import io.github.leopard.core.strategy.exception.StrategyExecuteException;
import io.github.leopard.core.strategy.model.ChoiceHigherTransactionParamDTO;
import io.github.leopard.core.strategy.model.TrackTransactionDTO;
import io.github.leopard.exchange.client.IExchangeApi;
import io.github.leopard.exchange.extension.GateApiExtension;
import io.github.leopard.exchange.model.dto.request.EatSpotOrderMarketRequestDTO;
import io.github.leopard.exchange.model.dto.request.SpotAccountRequestDTO;
import io.github.leopard.exchange.model.dto.request.SpotPriceTriggeredOrderRequestDTO;
import io.github.leopard.exchange.model.dto.result.CandlestickResultDTO;
import io.github.leopard.exchange.model.dto.result.EatSpotOrderMarketResultDTO;
import io.github.leopard.exchange.model.dto.result.SpotAccountResultDTO;
import io.github.leopard.exchange.model.dto.result.TickResultDTO;
import io.github.leopard.exchange.model.enums.SideEnum;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 *
 * @author meteor
 */
@Component("choiceHigherTrackStrategy")
public class ChoiceHigherTransactionStrategy extends AbstractTrackCandlestickStrategy<TrackTransactionDTO, ChoiceHigherTransactionParamDTO, EatSpotOrderMarketResultDTO>{

    @Override
    public void execute(IExchangeApi exchangeApi, StrategyParam<String, String> param) throws StrategyExecuteException {
        super.execute(exchangeApi, param);
    }

    /**
     * 构造监控参数
     * @param param
     * @return
     */
    @Override
    protected ChoiceHigherTransactionParamDTO buildMonitoringParam(StrategyParam<String, String> param) {
        ChoiceHigherTransactionParamDTO transactionParam = JSON.parseObject(JSON.toJSONString(param),ChoiceHigherTransactionParamDTO.class);
        if(CollectionUtils.isEmpty(transactionParam.getFilter_list())){
            transactionParam.setFilter_list(CurrencyConstants.FILTER_CURRENCY);
        }else{
            transactionParam.getFilter_list().addAll(CurrencyConstants.FILTER_CURRENCY);
        }
        return transactionParam;
    }

    /**
     * 计算需要查询的币种
     * @param api
     * @param monitoringParam
     * @return
     */
    @Override
    protected List<String> fetchCurrencyPriceList(GateApiExtension api, ChoiceHigherTransactionParamDTO monitoringParam) {
        //排名
        Integer limit_ranking = StringUtils.isBlank(monitoringParam.getLimit_ranking()) ? null:Integer.parseInt(monitoringParam.getLimit_ranking());
        //总交易量
        BigDecimal quote_volume  =  StringUtils.isBlank(monitoringParam.getPurchase_amount()) ? null:new BigDecimal (monitoringParam.getPurchase_amount());
        //过滤的曲线
        List<String> filterList = monitoringParam.getFilter_list();
        List<TickResultDTO> tickResultDTOS = api.fetchRankingTickerList(limit_ranking, quote_volume, filterList);
        List<String> currencyList = tickResultDTOS.stream().map(TickResultDTO::getCurrencyPair).collect(Collectors.toList());
        return currencyList;
    }

    /**
     * 计算查询结果
     * @param monitoringParam
     * @param market 市场币种
     * @return
     */
    @Override
    protected TrackTransactionDTO computeMonitoringResult(ChoiceHigherTransactionParamDTO monitoringParam, String market) {
        GateApiExtension client = GateApiExtension.create();
        BigDecimal changePercentage = client.fetchCurrencyTodayChangePercentage(market);
        //预期的涨幅
        BigDecimal expectChangePercentage = new BigDecimal(monitoringParam.getRetreat_ratio());
        //符合预期
        if(changePercentage.compareTo(expectChangePercentage)>=0){
            TrackTransactionDTO trackTransactionDTO = new TrackTransactionDTO();
            trackTransactionDTO.setBuy(Boolean.TRUE);
            trackTransactionDTO.setMarket(market);
            trackTransactionDTO.setPurchase_amount(monitoringParam.purchase_amount);
            return trackTransactionDTO;
        }
        return null;
    }

    @Override
    protected Boolean specialProcess(TrackTransactionDTO dataResult,GateApiExtension api) {
        // 计算结果不允许购买
        if(!dataResult.getBuy()){
            return Boolean.FALSE;
        }
        //判断账户是否有usdt,没用的话就不交易了
        SpotAccountResultDTO spotAccountResultDTO = api.spotAccountMust(CurrencyConstants.USDT);
        //账户金额不够,不交易
        if(spotAccountResultDTO.getAvailable().compareTo(new BigDecimal(dataResult.getPurchase_amount()))<0){
            return Boolean.FALSE;
        }
        return true;
    }

    @Override
    protected EatSpotOrderMarketResultDTO transactionProcess(TrackTransactionDTO dataResult, GateApiExtension api) {
        EatSpotOrderMarketRequestDTO request = new EatSpotOrderMarketRequestDTO();
        request.setSideEnum(SideEnum.BUY);
        request.setMarket(dataResult.getMarket());
        request.setUsdtAmt(new BigDecimal(dataResult.getPurchase_amount()));
        //下单成功
        EatSpotOrderMarketResultDTO eatSpotOrderMarketResult = api.eatSpotOrderMarketMust(request);

        return eatSpotOrderMarketResult;
    }

    @Override
    protected String buildWxMessage(TrackTransactionDTO dataResult, EatSpotOrderMarketResultDTO transaction) {
        return null;
    }

    @Override
    protected void sendWxMessage(String contentMsg) {

    }


}
