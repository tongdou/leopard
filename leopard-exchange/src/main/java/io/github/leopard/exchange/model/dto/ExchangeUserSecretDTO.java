package io.github.leopard.exchange.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 交易所用户密钥信息
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeUserSecretDTO {

    /**
     * gate api key
     */
    private String key;
    /**
     * gate 秘钥
     */
    private String secret;


}
