package io.github.leopard.core.strategy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Stopwatch;
import io.github.leopard.common.constant.Constants;
import io.github.leopard.common.utils.CurrencyUtils;
import io.github.leopard.common.utils.StringUtils;
import io.github.leopard.core.strategy.exception.StrategyExecuteException;
import io.github.leopard.core.strategy.exception.StrategyResultCodeEnum;
import io.github.leopard.core.strategy.model.UserSecretDTO;
import io.github.leopard.exchange.extension.GateApiExtension;
import io.github.leopard.exchange.model.dto.ExchangeUserSecretDTO;
import io.github.leopard.system.domain.BizStrategy;
import io.github.leopard.system.domain.BizStrategyUser;
import io.github.leopard.system.service.IBizStrategyService;
import io.github.leopard.system.service.IBizStrategyUserService;
import java.util.concurrent.TimeUnit;
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
@Component("strategyExecutors")
@Slf4j
public class StrategyExecutors extends IExecutors {


    @Resource
    private IBizStrategyUserService strategyUserService;
    @Resource
    private IBizStrategyService strategyService;


    private ApplicationContext springContext;

    @Autowired
    public void setSpringContext(ApplicationContext springContext) {
        this.springContext = springContext;
    }


    public void execute(String userStrategyId) throws StrategyExecuteException {

        BizStrategyUser strategyUser = this.strategyUserService.selectBizStrategyUserById(userStrategyId);
        if (strategyUser == null) {
            throw new StrategyExecuteException(StrategyResultCodeEnum.NOT_FOUND);
        }

        BizStrategy strategy = this.strategyService.selectBizStrategyById(strategyUser.getStrategyId());
        if (strategy == null) {
            throw new StrategyExecuteException(StrategyResultCodeEnum.NOT_FOUND);
        }

        String beanName = strategy.getBeanName();
        String className = strategy.getClassName();

        IStrategy strategyInterface = null;

        if (StringUtils.isNotEmpty(beanName)) {
            try {
                strategyInterface = (IStrategy) springContext.getBean(beanName);
            } catch (NullPointerException npe) {
                throw new StrategyExecuteException(StrategyResultCodeEnum.NOT_FOUND);
            }
        }

        if (strategyInterface == null) {
            throw new StrategyExecuteException(StrategyResultCodeEnum.NOT_FOUND);
        }

        StrategyParam<String, String> params = JSON
                .parseObject(strategyUser.getConfigJson(), new TypeReference<StrategyParam<String, String>>() {
                });

        UserSecretDTO userSecret = buildUserSecret(strategyUser.getUid());

        ExchangeUserSecretDTO exchangeUserSecretDTO = new ExchangeUserSecretDTO();
        exchangeUserSecretDTO.setKey(userSecret.getKey());
        exchangeUserSecretDTO.setSecret(userSecret.getSecret());
        GateApiExtension client = GateApiExtension.auth(exchangeUserSecretDTO);

        Stopwatch started = Stopwatch.createStarted();
        try {
            params.put(Constants.WX_UID,userSecret.getWxUid());
            strategyInterface.execute(client, params);
        } catch (StrategyExecuteException e) {
            log.error("用户策略执行异常[{}][{}]", JSON.toJSON(strategyUser), e.getMsg());
        }

        log.info("用户策略执行结束，耗时[{}s]", started.elapsed(TimeUnit.SECONDS));

    }


}
