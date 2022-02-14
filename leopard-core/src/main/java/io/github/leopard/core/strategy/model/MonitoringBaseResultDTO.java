package io.github.leopard.core.strategy.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * 监控币种涨跌情况 - 基础类定义
 *
 * @description:
 * @author: liuxin79
 * @date: 2022-02-10 16:14
 */
public class MonitoringBaseResultDTO implements Serializable {

    /**
     * 是否下跌
     */
    private Boolean isFall;

    /**
     * 振幅比例
     */
    private String amplitudeRatio;

    /**
     * 振幅金额
     */
    private String amplitudeAmount;


    /**
     * 监控周期
     */
    private String monitoringCycle;

    public Boolean getFall() {
        return isFall;
    }

    public void setFall(Boolean fall) {
        isFall = fall;
    }

    public String getAmplitudeRatio() {
        return amplitudeRatio;
    }

    public void setAmplitudeRatio(String amplitudeRatio) {
        this.amplitudeRatio = amplitudeRatio;
    }

    public String getAmplitudeAmount() {
        return amplitudeAmount;
    }

    public void setAmplitudeAmount(String amplitudeAmount) {
        this.amplitudeAmount = amplitudeAmount;
    }

    public String getMonitoringCycle() {
        return monitoringCycle;
    }

    public void setMonitoringCycle(String monitoringCycle) {
        this.monitoringCycle = monitoringCycle;
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
