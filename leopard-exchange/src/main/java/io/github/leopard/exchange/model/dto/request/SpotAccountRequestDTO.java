package io.github.leopard.exchange.model.dto.request;

import io.github.leopard.common.utils.ToJSON;
import lombok.Data;

/**
 * 现货账户查询
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Data
public class SpotAccountRequestDTO implements ToJSON {

    //USDT ETH 不是交易对
    private String currency;
}
