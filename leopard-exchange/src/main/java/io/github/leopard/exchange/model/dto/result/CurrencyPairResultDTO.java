package io.github.leopard.exchange.model.dto.result;

import io.github.leopard.common.utils.ToJSON;
import io.github.leopard.exchange.model.enums.TradeStatusEnum;
import lombok.Data;

/**
 * 查询币种信息
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Data
public class CurrencyPairResultDTO implements ToJSON {

    private String id;
    private String base;
    private String quote;
    public static final String SERIALIZED_NAME_FEE = "fee";
    private String fee;
    private String minBaseAmount;
    private String minQuoteAmount;
    private Integer amountPrecision;
    private Integer precision;
    private TradeStatusEnum tradeStatusEnum;
    private Long sellStart;
    private Long buyStart;

}
