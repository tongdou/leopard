package io.github.leopard.exchange.model.enums;

import lombok.Getter;

/**
 * K线周期
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
public enum CandlesticksIntervalEnum {


    S_10("10s"),
    M_1("1m"),
    M_5("5m"),
    M_15("15m"),
    M_30("30m"),
    H_1("1h"),
    H_4("4h"),
    H_8("8h"),
    D_1("1d"),
    D_7("7d"),
    ;

    @Getter
    private String value;

    CandlesticksIntervalEnum(String value) {
        this.value = value;
    }
}
