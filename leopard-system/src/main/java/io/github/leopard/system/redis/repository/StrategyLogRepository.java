package io.github.leopard.system.redis.repository;

import io.github.leopard.system.redis.domain.StrategyLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface StrategyLogRepository extends CrudRepository<StrategyLog, String> {

}