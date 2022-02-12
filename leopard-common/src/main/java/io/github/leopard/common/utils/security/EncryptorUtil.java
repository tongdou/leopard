package io.github.leopard.common.utils.security;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * AES加解密工具类
 */
public class EncryptorUtil {

    public static StringEncryptor stringEncryptor() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        //秘钥
        encryptor.setPassword("leopard");
        return encryptor;
    }
}
