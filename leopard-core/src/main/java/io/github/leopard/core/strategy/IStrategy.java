package io.github.leopard.core.strategy;

import io.github.leopard.common.utils.security.EncryptorUtil;
import io.github.leopard.exchange.client.IExchangeApi;
import io.github.leopard.exchange.model.dto.UserSecretDTO;
import io.github.leopard.system.domain.BizMerchantConfig;
import io.github.leopard.system.service.IBizMerchantConfigService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 策略接口
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
public interface IStrategy {

    void execute(IExchangeApi exchangeApi, StrategyParam<String, String> param) throws StrategyException;
}
