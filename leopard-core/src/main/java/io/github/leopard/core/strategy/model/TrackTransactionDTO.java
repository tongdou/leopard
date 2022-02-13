package io.github.leopard.core.strategy.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 监控币种振幅结果 指定币种
 *
 * @description:
 * @author: liuxin79
 * @date: 2022-02-10 16:14
 */
public class TrackTransactionDTO implements Serializable {

    /**
     * 币种
     */
    private String market;

    /**
     * 是否可以买入
     */
    private Boolean isBuy;

    /**
     * 购买金额
     * @return
     */
    private String purchase_amount;


    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public Boolean getBuy() {
        return isBuy;
    }

    public void setBuy(Boolean buy) {
        isBuy = buy;
    }

    public String getPurchase_amount() {
        return purchase_amount;
    }

    public void setPurchase_amount(String purchase_amount) {
        this.purchase_amount = purchase_amount;
    }
}
