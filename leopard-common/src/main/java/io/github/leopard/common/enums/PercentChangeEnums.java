package io.github.leopard.common.enums;

/**
 * @description:
 * @author: liuxin79
 * @date: 2022-02-10 11:04
 */
public enum PercentChangeEnums {

    ALL("0", "全部"),
    UPWARD ("1", "上涨"),
    DOWNWARD("2", "下跌");

    private final String code;
    private final String info;

    PercentChangeEnums(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }

}
