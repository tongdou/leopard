package io.github.leopard.exchange.model.dto.request;

import io.github.leopard.exchange.model.enums.SideEnum;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 市价吃单
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Data
public class EatSpotOrderMarketRequestDTO {

    private String market; //btc_usdt
    private BigDecimal usdtAmt; //总金额
    private SideEnum sideEnum; //委托方向
    private String text;
    private Integer retryMax = 10; //重试次数

}
