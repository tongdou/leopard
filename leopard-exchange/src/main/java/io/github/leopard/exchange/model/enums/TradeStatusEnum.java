package io.github.leopard.exchange.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

public enum TradeStatusEnum {

    UNTRADABLE("untradable"),
    BUYABLE("buyable"),
    SELLABLE("sellable"),
    TRADABLE("tradable");

    @Getter
    private String value;

    private TradeStatusEnum(String value) {
        this.value = value;
    }

    public static TradeStatusEnum toEnum(String value) {
        for (TradeStatusEnum statusEnum : TradeStatusEnum.values()) {
            if (StringUtils.equals(value, statusEnum.getValue())) {
                return statusEnum;
            }
        }
        return null;
    }
}