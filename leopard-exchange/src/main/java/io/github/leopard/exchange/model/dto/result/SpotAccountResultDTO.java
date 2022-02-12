package io.github.leopard.exchange.model.dto.result;

import io.github.leopard.common.utils.ToJSON;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 现货账户查询
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Data
public class SpotAccountResultDTO implements ToJSON {

    private String currency; //指定币种
    private BigDecimal available; //可用余额
    private BigDecimal locked; //冻结金额
}
