package io.github.leopard.exchange.model.dto.request;

import io.github.leopard.exchange.model.enums.RuleEnum;
import io.github.leopard.exchange.model.enums.SideEnum;
import io.github.leopard.exchange.model.enums.TimeInForceEnum;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 触发单
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Data
public class SpotPriceTriggeredOrderRequestDTO {

    private String market;

    //触发条件
    private BigDecimal triggerPrice; //触发价格
    private RuleEnum triggerRule;  //触发规则
    private Integer triggerExpiration; //触发条件过期时间，单位秒

    //委托条件
    private BigDecimal price; //委托价格
    private BigDecimal tokenAmt;//委托token数量
    private SideEnum sideEnum; //委托方向
    private TimeInForceEnum timeInForceEnum; //委托策略

}
