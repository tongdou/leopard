package io.github.leopard.exchange.model.dto.result;

import io.github.leopard.common.utils.ToJSON;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * TICKER信息
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Data
public class TickResultDTO implements ToJSON {

    private BigDecimal last;
    private BigDecimal baseVolume;
    private BigDecimal quoteVolume;
    private BigDecimal high24h;
    private BigDecimal low24h;
    private BigDecimal highestBid;
    private BigDecimal lowestAsk;
    private String currencyPair;
    private String changePercentage;

}
