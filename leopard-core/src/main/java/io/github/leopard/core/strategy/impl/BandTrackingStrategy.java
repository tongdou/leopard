package io.github.leopard.core.strategy.impl;

import com.alibaba.fastjson.JSON;
import io.github.leopard.common.utils.BigDecimalUtil;
import io.github.leopard.common.utils.CommonUtils;
import io.github.leopard.core.strategy.StrategyParam;
import io.github.leopard.core.strategy.exception.StrategyExecuteException;
import io.github.leopard.core.strategy.exception.StrategyResultCodeEnum;
import io.github.leopard.exchange.client.IExchangeApi;
import io.github.leopard.exchange.extension.GateApiExtension;
import io.github.leopard.exchange.extension.MarketMonitor;
import io.github.leopard.exchange.extension.MarketMonitor.CandlestickMonitorStatus;
import io.github.leopard.exchange.model.dto.request.EatSpotOrderMarketRequestDTO;
import io.github.leopard.exchange.model.dto.result.EatSpotOrderMarketResultDTO;
import io.github.leopard.exchange.model.dto.result.SpotAccountResultDTO;
import io.github.leopard.exchange.model.enums.CandlesticksIntervalEnum;
import io.github.leopard.exchange.model.enums.SideEnum;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 波段追踪明
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Slf4j
@Component("bandTrackingStrategy")
public class BandTrackingStrategy extends AbstractStrategy {

    public static final String MARKET = "market";
    public static final String MAX_PULL_BACK = "maxPullBack";
    public static final String UP_PERCENT = "upPercent";
    public static final String USDT_AMT = "usdtAmt";
    public static final String CANDLESTICK_INTERVAL = "candlesticksInterval";


    @Override
    public void execute(IExchangeApi exchangeApi, StrategyParam<String, String> param) throws StrategyExecuteException {

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
                return CandlestickMonitorStatus.STOP;
            }

            return CandlestickMonitorStatus.RUNNING;
        });

        try {
            this.doLoopBiz(api, market, maxPullBack, usdtAmt);
        } catch (StrategyExecuteException e) {
            log.error("[{}][{}] 策略执行异常[{}]", market, intervalEnum.getValue(), e.getMsg());
            throw e;
        }

    }


    private void doLoopBiz(GateApiExtension api, String market, BigDecimal maxPullBack, BigDecimal usdtAmt)
            throws StrategyExecuteException {

        SpotAccountResultDTO usdtAccount = api.spotAccountMust("USDT");
        if (usdtAccount.getAvailable().compareTo(usdtAmt) < 0) {
            throw new StrategyExecuteException(StrategyResultCodeEnum.ACCOUNT_NOT_ENOUGH);
        }

        //吃单 TODO 优化下单金额 效率 滑点
        EatSpotOrderMarketRequestDTO marketRequestDTO = new EatSpotOrderMarketRequestDTO();
        marketRequestDTO.setMarket(market);
        marketRequestDTO.setSideEnum(SideEnum.BUY);
        marketRequestDTO.setUsdtAmt(usdtAmt);
        EatSpotOrderMarketResultDTO orderMarketResultDTO = api.eatSpotOrderMarketMustOrNull(marketRequestDTO);

        new BandTrackingStrategySupport(api).syncExecute(
                market,
                orderMarketResultDTO.getCost(),
                orderMarketResultDTO.getTokenNumber(),
                BigDecimalUtil.roundingHalfUp(maxPullBack.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP)),
                CandlesticksIntervalEnum.M_5);
    }
}
