package io.github.leopard.exchange.model.dto.request;

import lombok.Data;

/**
 * 取消触发订单
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Data
public class CancelSpotPriceTriggeredOrderRequestDTO {

    private String orderId;
}
