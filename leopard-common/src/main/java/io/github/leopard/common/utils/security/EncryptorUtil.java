package io.github.leopard.common.utils.security;

import io.github.leopard.common.constant.Constants;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * AES加解密工具类
 */
public class EncryptorUtil {

    private static StringEncryptor stringEncryptor() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        //秘钥
        encryptor.setPassword(Constants.ENCRYPTOR);
        return encryptor;
    }

    /**
     * 加密
     * @param message
     * @return
     */
    public static String encrypt(String message) {
        return stringEncryptor().encrypt(message);
    }


    /**
     * 解密
     * @param encryptedMessage
     * @return
     */
    public static String decrypt(String encryptedMessage) {
        return stringEncryptor().decrypt(encryptedMessage);
    }
}
