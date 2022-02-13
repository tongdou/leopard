package io.github.leopard;

import io.github.leopard.core.strategy.StrategyException;
import io.github.leopard.core.strategy.StrategyExecutors;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StrategyExecutorsTest extends AbstractBaseTest {

    @Autowired
    private StrategyExecutors strategyExecutors;

    @Test
    public void executeTest() throws StrategyException {
        strategyExecutors.execute("1");
    }
}
