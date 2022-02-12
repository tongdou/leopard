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
    private String baseVolume;
    private String quoteVolume;
    private String high24h;
    private String low24h;
    private String highestBid;
    private String lowestAsk;
    private String currencyPair;
}
