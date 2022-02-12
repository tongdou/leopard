package io.github.leopard.exchange.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户密钥信息
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Data
@AllArgsConstructor
public class UserSecretDTO {

    private String key;
    private String secret;

}
