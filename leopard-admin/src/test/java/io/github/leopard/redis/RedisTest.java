package io.github.leopard.redis;

import com.alibaba.fastjson.JSON;
import io.github.leopard.AbstractBaseTest;
import io.github.leopard.system.redis.domain.StrategyLog;
import io.github.leopard.system.redis.repository.StrategyLogRepository;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
public class RedisTest extends AbstractBaseTest {

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

//    @Resource
//    private StrategyLogRepository logRepository;

    @Test
    public void test(){

        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();


        opsForValue.set("hehe","1");
        redisTemplate.expire("hehe",1, TimeUnit.HOURS);


        Object fw = opsForValue.get("hehe");
        System.out.println(fw);

        StrategyLog strategyLog = new StrategyLog();
        strategyLog.setStrategyId("1");

//        logRepository.save(strategyLog);
//
//        Optional<StrategyLog> prev = logRepository.findById("1");
//
//        if(prev.isPresent()){
//            StrategyLog strategyLog1 = prev.get();
//            System.out.println(JSON.toJSONString(strategyLog1));
//        }

    }

}
