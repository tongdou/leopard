package io.github.leopard.core.strategy.model;

import java.util.List;

/**
 *
 * 追踪买的参数配置类
 * @author meteor
 */
public class ChoiceHigherTransactionParamDTO {

    /**
     * 上涨百分比
     */
    public  String upward_percent;

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
    public  String limit_ranking;

    /**
     * 买入进入 usdt
     */
    public String usdt_amount;

    /**
     * 据最高点回撤比例
     */
    public  String  retreat_ratio;


    /**
     * 需要过滤的币种,可以为空
     */
    private List<String> filter_list;

    public String getUpward_percent() {
        return upward_percent;
    }

    public void setUpward_percent(String upward_percent) {
        this.upward_percent = upward_percent;
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

    public String getUsdt_amount() {
        return usdt_amount;
    }

    public void setUsdt_amount(String usdt_amount) {
        this.usdt_amount = usdt_amount;
    }

    public String getRetreat_ratio() {
        return retreat_ratio;
    }

    public void setRetreat_ratio(String retreat_ratio) {
        this.retreat_ratio = retreat_ratio;
    }

    public List<String> getFilter_list() {
        return filter_list;
    }

    public void setFilter_list(List<String> filter_list) {
        this.filter_list = filter_list;
    }

    public String getQuote_volume() {
        return quote_volume;
    }

    public void setQuote_volume(String quote_volume) {
        this.quote_volume = quote_volume;
    }
}
