package io.github.leopard.core.strategy.impl;

import io.github.leopard.common.utils.BigDecimalUtil;
import io.github.leopard.core.strategy.IStrategy;
import io.github.leopard.core.strategy.StrategyException;
import io.github.leopard.core.strategy.StrategyResultCodeEnum;
import io.github.leopard.exchange.model.dto.result.CandlestickResultDTO;
import io.github.leopard.exchange.model.dto.result.SpotAccountResultDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class AbstractStrategy implements IStrategy {


    /**
     * 获取周期的涨幅
     */
    protected BigDecimal changePercent(CandlestickResultDTO cur) {
        BigDecimal percent = (cur.getClose().divide(cur.getOpen(), 8, RoundingMode.HALF_UP).subtract(new BigDecimal("1")))
                .multiply(new BigDecimal("100"));
        return BigDecimalUtil.roundingHalfUp(percent);
    }



}
