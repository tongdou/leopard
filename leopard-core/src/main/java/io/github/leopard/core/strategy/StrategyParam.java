package io.github.leopard.core.strategy;

import java.math.BigDecimal;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

/**
 * 策略参数
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
public class StrategyParam<K, V> extends HashMap {

    public BigDecimal getBigDecimal(String key) {
        String val = getString(key);
        if (StringUtils.isBlank(val)) {
            return null;
        }
        try {
            return new BigDecimal(val.trim());
        } catch (Throwable e) {
            return null;
        }
    }

    public Integer getInteger(String key) {
        String val = getString(key);
        if (StringUtils.isBlank(val)) {
            return null;
        }
        try {
            return Integer.parseInt(val.trim());
        } catch (Throwable e) {
            return null;
        }
    }


    public Integer getIntegerOrDefault(String key, Integer defaultVal) {
        Integer val = getInteger(key);
        return val == null ? defaultVal : val;
    }

    public BigDecimal getBigDecimalOrDefault(String key, BigDecimal defaultVal) {
        BigDecimal val = getBigDecimal(key);
        return val == null ? defaultVal : val;
    }

    public String getString(String key) {
        Object val = super.get(key);
        if (val == null) {
            return StringUtils.EMPTY;
        }
        return String.valueOf(val);
    }

    public String getStringOrDefault(String key, String defaultVal) {
        String val = getString(key);
        return StringUtils.isNotBlank(val) ? val : defaultVal;
    }


}
