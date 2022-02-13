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

    /**
     * gate api key
     */
    private String key;
    /**
     * gate 秘钥
     */
    private String secret;

    /**
     * 微信uid
     */
    private String wxUid;

}
