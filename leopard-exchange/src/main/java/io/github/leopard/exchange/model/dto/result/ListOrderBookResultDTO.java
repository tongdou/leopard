package io.github.leopard.exchange.model.dto.result;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

/**
 * 盘口深度
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Data
public class ListOrderBookResultDTO {

    private List<AskDTO> asks; //卖盘
    private List<BidDTO> bids; //买盘

    @Data
    public static class AskDTO {

        private BigDecimal price;
        private BigDecimal tokenAmt;
    }

    @Data
    public static class BidDTO {

        private BigDecimal price;
        private BigDecimal tokenAmt;
    }
}
