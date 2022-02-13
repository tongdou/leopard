package io.github.leopard.common.constant;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 币种常量类
 */
public class CurrencyConstants {

    public static final String BTC = "BTC";
    public static final String ETH = "ETH";
    public static final String STARL = "STARL";
    public static final String USDT = "USDT";
    //点卡
    public static final String POINT = "POINT";
    public static final String FORWARD = "FORWARD";
    //杠杆
    public static final String LEVERAGED_3L = "3L";
    public static final String LEVERAGED_3S = "3S";
    public static final String LEVERAGED_5L = "5L";
    public static final String LEVERAGED_5S = "5S";

    public static final String SYMBOL_ = "_";

    public static List<String> FILTER_CURRENCY = new ArrayList<>();
    static {
        FILTER_CURRENCY.add(LEVERAGED_3L);
        FILTER_CURRENCY.add(LEVERAGED_3S);
        FILTER_CURRENCY.add(LEVERAGED_5L);
        FILTER_CURRENCY.add(LEVERAGED_5S);
    }

}
