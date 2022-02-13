package io.github.leopard.exchange.model.dto.result;

import io.github.leopard.common.utils.ToJSON;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 市价吃单
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Data
public class EatSpotOrderMarketResultDTO implements ToJSON {

    private String market; //btc_usdt
    private BigDecimal price; //下单价格 并非实际成交价格
    private BigDecimal tokenAmt;//获得token数量
    private BigDecimal fee;//手续费
    private BigDecimal fillTotal; //已成交金额
    private String orderId;
    //成本价
    private BigDecimal cost;
    //实际的数量
    private BigDecimal tokenNumber;

}
