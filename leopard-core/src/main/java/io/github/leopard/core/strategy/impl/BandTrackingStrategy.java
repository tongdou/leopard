package io.github.leopard.core.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import io.gate.gateapi.models.SpotAccount;
import io.github.leopard.common.utils.CommonUtils;
import io.github.leopard.core.strategy.IStrategy;
import io.github.leopard.core.strategy.StrategyException;
import io.github.leopard.core.strategy.StrategyParam;
import io.github.leopard.core.strategy.StrategyResultCodeEnum;
import io.github.leopard.exchange.client.IExchangeApi;
import io.github.leopard.exchange.extension.GateApiExtension;
import io.github.leopard.exchange.extension.MarketMonitor;
import io.github.leopard.exchange.extension.MarketMonitor.TickerChangeListener;
import io.github.leopard.exchange.model.dto.Result;
import io.github.leopard.exchange.model.dto.request.CreateSpotOrderRequestDTO;
import io.github.leopard.exchange.model.dto.result.CandlestickResultDTO;
import io.github.leopard.exchange.model.dto.result.CreateSpotOrderResultDTO;
import io.github.leopard.exchange.model.dto.result.SpotAccountResultDTO;
import io.github.leopard.exchange.model.enums.CandlesticksIntervalEnum;
import io.github.leopard.exchange.model.enums.SideEnum;
import io.github.leopard.exchange.model.enums.TimeInForceEnum;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 波段追踪明
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Slf4j
@Component("bandTrackingStrategy")
public class BandTrackingStrategy extends AbstractStrategy {

    private static final String MARKET = "market";
    private static final String MAX_PULL_BACK = "maxPullBack";
    private static final String UP_PERCENT = "upPercent";
    private static final String USDT_AMT = "usdtAmt";
    private static final String CANDLESTICK_INTERVAL = "candlesticksInterval";


    @Override
    public void execute(IExchangeApi exchangeApi, StrategyParam<String, String> param) {

        GateApiExtension api = (GateApiExtension) exchangeApi;

        String market = param.getString(MARKET);
        BigDecimal maxPullBack = param.getBigDecimal(MAX_PULL_BACK);
        BigDecimal upPercent = param.getBigDecimal(UP_PERCENT);
        BigDecimal usdtAmt = param.getBigDecimal(USDT_AMT);
        CandlesticksIntervalEnum intervalEnum = CandlesticksIntervalEnum.toEnum(param.getString(CANDLESTICK_INTERVAL));

        MarketMonitor.syncMonitor(market, intervalEnum, (prev, cur, interval) -> {

            BigDecimal changePercent = this.changePercent(prev);
            log.info("[{}][{}][{}]周期内的涨幅为[{}%]，开盘[{}]，收盘[{}]", market, intervalEnum.getValue(), prev.getDateTimeString(),
                    changePercent,
                    prev.getOpenString(), prev.getCloseString());

            if (changePercent.compareTo(BigDecimal.ZERO) > 0 && changePercent.compareTo(upPercent) >= 0) {
                log.info("[{}][{}][{}] 你监控的币爆涨[{}%]", market, intervalEnum.getValue(), prev.getDateTimeString(), changePercent);

                try {
                    this.doLoopBiz(api, market, maxPullBack, usdtAmt);
                } catch (StrategyException e) {
                    log.error("[{}][{}][{}] 策略执行异常[{}]", market, intervalEnum.getValue(), prev.getDateTimeString(),
                            e.getMsg());
                }
                throw new InterruptedException();
            }

        });

    }


    private void doLoopBiz(GateApiExtension api, String market, BigDecimal maxPullBack, BigDecimal usdtAmt) throws StrategyException {

        SpotAccountResultDTO usdtAccount = api.spotAccountMust("USDT");
        if (usdtAccount.getAvailable().compareTo(usdtAmt) < 0) {
            throw new StrategyException(StrategyResultCodeEnum.ACCOUNT_NOT_ENOUGH);
        }

        //下单

        //获取最新价
        BigDecimal lastPrice = api.getTickerMust(market).getLast();

        CreateSpotOrderRequestDTO spotOrderRequestDTO = new CreateSpotOrderRequestDTO();
        spotOrderRequestDTO.setMarket(market);
        spotOrderRequestDTO.setSideEnum(SideEnum.BUY);
        spotOrderRequestDTO.setTimeInForceEnum(TimeInForceEnum.IOC);
        spotOrderRequestDTO.setPrice(lastPrice.add(new BigDecimal("0.00001")));

        // 计算大概能买多少个 四舍五入
        spotOrderRequestDTO.setTokenAmt(usdtAmt.divide(spotOrderRequestDTO.getPrice(), 8, BigDecimal.ROUND_HALF_UP));

        Result<CreateSpotOrderResultDTO> createSpotOrderResultDTOResult = api.createSpotOrder(spotOrderRequestDTO);
        if (!createSpotOrderResultDTOResult.isSuccess()) {
            throw new StrategyException(StrategyResultCodeEnum.SPOT_ORDER_FAIL);
        }

        // 有多少个不好算啊  TODO
        BigDecimal remain;
        while (true) {
            SpotAccountResultDTO marketAccount = api.spotAccountMust(market);
            if (Objects.nonNull(marketAccount)) {
                remain = marketAccount.getAvailable();
                if (remain.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                String messageBegin = String.format("[%s]准备开启策略，account=%s", market, JSON.toJSONString(marketAccount));
                log.info(messageBegin);
                break;
            }
            CommonUtils.sleepSeconds(3);
        }


        new BandTrackingStrategySupport(api).syncExecute(
                market,
                spotOrderRequestDTO.getPrice(),
                spotOrderRequestDTO.getTokenAmt(),
                maxPullBack,
                CandlesticksIntervalEnum.M_5);
    }


}
