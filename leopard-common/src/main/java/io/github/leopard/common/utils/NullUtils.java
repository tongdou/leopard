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


    public static String ifNullDefaultBlank(String val) {
        if (StringUtils.isBlank(val)) {
            return org.apache.commons.lang3.StringUtils.EMPTY;
        }
        return val;
    }
}
