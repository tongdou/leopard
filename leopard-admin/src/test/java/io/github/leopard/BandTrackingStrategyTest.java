package io.github.leopard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.leopard.core.strategy.StrategyParam;
import io.github.leopard.core.strategy.exception.StrategyExecuteException;
import io.github.leopard.core.strategy.impl.BandTrackingStrategy;
import io.github.leopard.exchange.extension.GateApiExtension;
import io.github.leopard.exchange.model.dto.ExchangeUserSecretDTO;
import io.github.leopard.exchange.model.enums.CandlesticksIntervalEnum;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.annotation.Resource;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
public class BandTrackingStrategyTest extends AbstractBaseTest {


    @Resource
    private BandTrackingStrategy strategy;

    @Test
    public void go_test() throws StrategyExecuteException, IOException {

        String s = FileUtils.readFileToString(new File("/Users/pleuvoir/store/08 btc/mine"), Charset.defaultCharset());

        JSONObject jsonObject = JSON.parseObject(s);

        ExchangeUserSecretDTO secretDTO = new ExchangeUserSecretDTO();
        secretDTO.setKey(jsonObject.getString("apiKey"));
        secretDTO.setSecret(jsonObject.getString("secretKey"));

        GateApiExtension apiExtension = GateApiExtension.auth(secretDTO);

        StrategyParam<String, String> strategyParam = new StrategyParam<>();
        strategyParam.put(BandTrackingStrategy.MARKET, "VRA_USDT");
        strategyParam.put(BandTrackingStrategy.USDT_AMT, "1.1");
        strategyParam.put(BandTrackingStrategy.CANDLESTICK_INTERVAL, CandlesticksIntervalEnum.M_5.getValue());
        strategyParam.put(BandTrackingStrategy.UP_PERCENT, "1");
        strategyParam.put(BandTrackingStrategy.MAX_PULL_BACK, "5");

        strategy.execute(apiExtension, strategyParam);
    }


}
