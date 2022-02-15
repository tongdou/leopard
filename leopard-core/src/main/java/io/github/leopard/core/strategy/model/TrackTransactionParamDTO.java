package io.github.leopard.core.strategy.model;

import java.util.List;

/**
 *
 * 追踪买卖的参数配置类
 * @author meteor
 */

public class TrackTransactionParamDTO {

    /**
     * 上涨百分比
     */
    public String upward_percent;

    /**
     * 总交易额
     */
    private String quote_volume;
    /**
     * 购买金额
     */
    public String purchase_amount;

    /**
     * 排名限制
     */
    public String limit_ranking;


    /**
     * 据最高点回撤比例
     */
    public String retreat_ratio;


    /**
     * 需要过滤的交易对,可以为空
     */
    private List<String> filter_market;

    /**
     * 指定交易对,可以为空
     */
    private List<String> special_market;

    public String getUpward_percent() {
        return upward_percent;
    }

    public void setUpward_percent(String upward_percent) {
        this.upward_percent = upward_percent;
    }

    public String getQuote_volume() {
        return quote_volume;
    }

    public void setQuote_volume(String quote_volume) {
        this.quote_volume = quote_volume;
    }

    public String getPurchase_amount() {
        return purchase_amount;
    }

    public void setPurchase_amount(String purchase_amount) {
        this.purchase_amount = purchase_amount;
    }

    public String getLimit_ranking() {
        return limit_ranking;
    }

    public void setLimit_ranking(String limit_ranking) {
        this.limit_ranking = limit_ranking;
    }

    public String getRetreat_ratio() {
        return retreat_ratio;
    }

    public void setRetreat_ratio(String retreat_ratio) {
        this.retreat_ratio = retreat_ratio;
    }

    public List<String> getFilter_market() {
        return filter_market;
    }

    public void setFilter_market(List<String> filter_market) {
        this.filter_market = filter_market;
    }

    public List<String> getSpecial_market() {
        return special_market;
    }

    public void setSpecial_market(List<String> special_market) {
        this.special_market = special_market;
    }
}
