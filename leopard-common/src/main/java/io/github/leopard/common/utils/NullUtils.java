package io.github.leopard.common.utils;

import java.math.BigDecimal;

/**
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
public class NullUtils {

    public static BigDecimal ifNullDefaultZero(String val) {
        if (StringUtils.isBlank(val)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(val.trim());
    }

}
