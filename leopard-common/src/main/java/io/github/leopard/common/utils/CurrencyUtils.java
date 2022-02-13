package io.github.leopard.common.utils;

import java.math.BigDecimal;

/**
 * @description:
 * @author: liuxin79
 * @date: 2022-02-11 17:23
 */
public class CurrencyUtils {

    /**
     * 判断涨跌百分比
     *
     * @param openPrice
     * @param currentPrice
     * @return
     */
    public static BigDecimal todayChangePercentage(BigDecimal openPrice, BigDecimal currentPrice) {
        //差价当期价格 - 开盘价格
        BigDecimal priceDifference = currentPrice.subtract(openPrice);
        return priceDifference.divide(openPrice, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
    }


    /**
     * 翻译上涨还是下跌
     *
     * @param isFall
     * @return
     */
    public static String convertDirection(boolean isFall) {
        return isFall ? "下跌" : "上涨";
    }

    /**
     * 翻译上涨还是下跌
     *
     * @param isFall
     * @return
     */
    public static String convertWarnMsg(boolean isFall) {
        return isFall ? "瀑布" : "暴涨";
    }

    /**
     * 翻译上涨还是下跌的颜色
     *
     * @param isFall
     * @return
     */
    public static String convertColorValue(boolean isFall) {
        return isFall ? "green" : "red";
    }

    /**
     * 转换时间周期
     *
     * @param monitoringCycle
     * @return
     */
    public static String convertMonitoringCycle(String monitoringCycle) {
        return monitoringCycle.replace("s", "秒").replace("m", "分钟").replace("h", "小时").replace("d", "天");
    }
}
