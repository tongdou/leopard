package io.github.leopard.core.strategy.tracktransaction;

import io.github.leopard.common.constant.LeopardConstants;
import io.github.leopard.core.strategy.StrategyExecuteException;
import io.github.leopard.core.strategy.StrategyParam;
import io.github.leopard.core.strategy.impl.AbstractStrategy;
import io.github.leopard.exchange.client.IExchangeApi;
import io.github.leopard.exchange.extension.GateApiExtension;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component("trackTransactionStrategy")
public class TrackTransactionStrategy extends AbstractStrategy {
    @Override
    public void execute(IExchangeApi exchangeApi, StrategyParam<String, String> param) throws StrategyExecuteException {
        GateApiExtension api = (GateApiExtension) exchangeApi;
        //市场,多值用逗号隔开,可以为空
        String marketArr = param.getString(LeopardConstants.MARKET);
        List<String> marketList = new ArrayList<>();
        if (StringUtils.isNotBlank(marketArr)) {
            marketList = Arrays.asList(marketArr.split(","));
        }
        //交易总金额
        BigDecimal trade_amount = param.getBigDecimal(LeopardConstants.TRADE_AMOUNT);
        //上涨比例
        BigDecimal upward_percent = param.getBigDecimal(LeopardConstants.UPWARD_PERCENT);
        //买入数量
        BigDecimal usdt_amount = param.getBigDecimal(LeopardConstants.USDT_AMOUNT);

    }
}
