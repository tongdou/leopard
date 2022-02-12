package io.github.leopard.exchange.model.dto.request;

import io.github.leopard.exchange.model.enums.CandlesticksIntervalEnum;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 获取上一周期的蜡烛图
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Data
public class PrevCandlestickRequestDTO {

    private String market;
    private CandlesticksIntervalEnum intervalEnum;
    private LocalDateTime curDateTime;

}
