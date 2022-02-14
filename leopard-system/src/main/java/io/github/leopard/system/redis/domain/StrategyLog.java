package io.github.leopard.system.redis.domain;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.TimeToLive;


@Data
public class StrategyLog {


    @Id
    private String strategyId; //

    private LocalDateTime createTime;

    @TimeToLive(unit = TimeUnit.HOURS)
    private Long ttl = 24L; // 缓存存在时间（1天）

}