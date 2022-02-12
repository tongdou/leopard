package io.github.leopard.core.strategy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.github.leopard.common.utils.StringUtils;
import io.github.leopard.exchange.extension.GateApiExtension;
import io.github.leopard.exchange.model.dto.UserSecretDTO;
import io.github.leopard.system.domain.BizStrategy;
import io.github.leopard.system.domain.BizStrategyUser;
import io.github.leopard.system.service.IBizMerchantConfigService;
import io.github.leopard.system.service.IBizStrategyService;
import io.github.leopard.system.service.IBizStrategyUserService;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 策略执行器
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Component
@Slf4j
public class StrategyExecutors {


    @Resource
    private IBizStrategyUserService strategyUserService;
    @Resource
    private IBizStrategyService strategyService;
    @Resource
    private IBizMerchantConfigService merchantConfigService;


    private ApplicationContext springContext;

    @Autowired
    public void setSpringContext(ApplicationContext springContext) {
        this.springContext = springContext;
    }


    public IStrategy execute(String strategyId) throws StrategyException {

        BizStrategyUser strategyUser = this.strategyUserService.selectBizStrategyUserById(strategyId);
        if (strategyUser == null) {
            throw new StrategyException(StrategyResultCodeEnum.NOT_FOUND);
        }

        BizStrategy strategy = this.strategyService.selectBizStrategyById(strategyUser.getId());
        if (strategy == null) {
            throw new StrategyException(StrategyResultCodeEnum.NOT_FOUND);
        }

        String beanName = strategy.getBeanName();
        String className = strategy.getClassName();

        IStrategy strategyInterface = null;

        if (StringUtils.isNotEmpty(beanName)) {
            try {
                strategyInterface = (IStrategy) springContext.getBean(beanName);
            } catch (NullPointerException npe) {
                throw new StrategyException(StrategyResultCodeEnum.NOT_FOUND);
            }
        }

        if (strategyInterface == null) {
            throw new StrategyException(StrategyResultCodeEnum.NOT_FOUND);
        }

        StrategyParam<String, String> params = JSON.parseObject(strategyUser.getConfigJson(), new TypeReference<StrategyParam<String, String>>() {
        });

        UserSecretDTO userSecret = new UserSecretDTO("", "");
        GateApiExtension client = GateApiExtension.auth(userSecret);


        strategyInterface.execute(client, params);


        //TODO
        return null;

    }

}
