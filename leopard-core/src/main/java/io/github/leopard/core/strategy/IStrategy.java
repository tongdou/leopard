package io.github.leopard.core.strategy;

import io.github.leopard.exchange.client.IExchangeApi;

/**
 * 策略接口
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
public interface IStrategy {

    void execute(IExchangeApi exchangeApi, StrategyParam<String, String> param) throws StrategyException;
}
