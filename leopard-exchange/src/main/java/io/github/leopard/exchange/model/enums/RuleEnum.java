package io.github.leopard.exchange.model.enums;

import lombok.Getter;

public enum RuleEnum {

    GREATER_THAN_OR_EQUAL_TO(">="),
    LESS_THAN_OR_EQUAL_TO("<=");

    @Getter
    private String value;

    private RuleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}