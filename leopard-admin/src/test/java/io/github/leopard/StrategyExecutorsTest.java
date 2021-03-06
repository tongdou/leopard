package io.github.leopard;

import io.github.leopard.core.strategy.exception.StrategyExecuteException;
import io.github.leopard.core.strategy.StrategyExecutors;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StrategyExecutorsTest extends AbstractBaseTest {

    @Autowired
    private StrategyExecutors strategyExecutors;

    @Test
    public void executeTest() throws StrategyExecuteException {
        strategyExecutors.execute("1");
    }
}
