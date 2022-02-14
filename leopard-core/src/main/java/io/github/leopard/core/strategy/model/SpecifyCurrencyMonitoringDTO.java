package io.github.leopard.core.strategy.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 监控币种振幅结果 指定币种
 *
 * @description:
 * @author: liuxin79
 * @date: 2022-02-10 16:14
 */
public class SpecifyCurrencyMonitoringDTO implements Serializable {

    /**
     * 币种
     */
    private String currency;

    /**
     * 当前价格
     */
    private BigDecimal currentPrice;

    /**
     * 监控币种数据 基础类
     */
    private List<MonitoringBaseResultDTO> monitoringBaseResultList;

    /**
     * 今日涨幅
     */
    private String changePercentageToday;

    /**
     * 24小时涨幅
     */
    private String changePercentage24;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public List<MonitoringBaseResultDTO> getMonitoringBaseResultList() {
        return monitoringBaseResultList;
    }

    public void setMonitoringBaseResultList(List<MonitoringBaseResultDTO> monitoringBaseResultList) {
        this.monitoringBaseResultList = monitoringBaseResultList;
    }

    public String getChangePercentageToday() {
        return changePercentageToday;
    }

    public void setChangePercentageToday(String changePercentageToday) {
        this.changePercentageToday = changePercentageToday;
    }

    public String getChangePercentage24() {
        return changePercentage24;
    }

    public void setChangePercentage24(String changePercentage24) {
        this.changePercentage24 = changePercentage24;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
