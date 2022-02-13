package io.github.leopard.exchange.model.dto.result;

import java.math.BigDecimal;
import lombok.Data;

/**
 * 市价吃单
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Data
public class EatSpotOrderMarketResultDTO {

    private String market; //btc_usdt
    private BigDecimal price; //下单价格 并非实际成交价格
    private BigDecimal tokenAmt;//获得token数量

}
