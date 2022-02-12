package io.github.leopard.exchange.model.dto.result;

import io.github.leopard.common.utils.ToJSON;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 蜡烛图
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Data
public class CandlestickResultDTO implements ToJSON {

    private String market; //交易对
    private String timestampString; //unix时间戳
    private String dateTimeString; //时间周期  由时间戳转换而来
    private String volumeString;  //交易额，计价货币 USDT等
    private String closeString; //收盘价
    private String highestString; //最高价
    private String lowestString; //最低价
    private String openString; //开盘价
    private String desc; //描述


    private LocalDateTime dateTime; //时间周期
    private BigDecimal volume;  //交易额，计价货币 USDT等
    private BigDecimal close; //收盘价
    private BigDecimal highest; //最高价
    private BigDecimal lowest; //最低价
    private BigDecimal open; //开盘价

}
