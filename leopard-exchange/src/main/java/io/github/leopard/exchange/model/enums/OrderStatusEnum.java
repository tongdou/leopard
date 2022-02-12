package io.github.leopard.exchange.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 订单状态
 */
public enum OrderStatusEnum {

    UNKNOWN("unknown"),
    OPEN("open"),
    CLOSED("closed"),
    CANCELLED("cancelled");

    @Getter
    private String name;

    OrderStatusEnum(String name) {
        this.name = name;
    }

    public static OrderStatusEnum toEnum(String name) {
        for (OrderStatusEnum value : OrderStatusEnum.values()) {
            if (StringUtils.equals(name, value.getName())) {
                return value;
            }
        }
        return null;
    }
}