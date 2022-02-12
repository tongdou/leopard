package io.github.leopard.exchange.model.dto.result;

import io.github.leopard.exchange.model.enums.OrderStatusEnum;
import lombok.Data;

/**
 * 挂单结果
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Data
public class CreateSpotOrderResultDTO {


    private OrderStatusEnum orderStatusEnum; //状态
}
