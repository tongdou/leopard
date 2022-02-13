package io.github.leopard.exchange.model.dto.request;

import lombok.Data;

/**
 * 现货订单查询
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Data
public class SpotOderQueryRequestDTO {

    private String orderId;
    private String market;
}
