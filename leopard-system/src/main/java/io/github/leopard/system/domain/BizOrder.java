package io.github.leopard.system.domain;

import io.github.leopard.common.core.domain.BaseEntity;
import java.math.BigDecimal;
import io.github.leopard.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 订单信息对象 biz_order
 * 
 * @author admin
 * @date 2022-02-12
 */
public class BizOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** uid */
    @Excel(name = "uid")
    private String uid;

    /** 通道名称 */
    @Excel(name = "通道名称")
    private String channelName;

    /** 订单状态 */
    @Excel(name = "订单状态")
    private String status;

    /** 市场 */
    @Excel(name = "市场")
    private String market;

    /** 计价货币委托金额 */
    @Excel(name = "计价货币委托金额")
    private BigDecimal quoteAmt;

    /** 标准货币数量 */
    @Excel(name = "标准货币数量")
    private BigDecimal tokenAmt;

    /** 下单价格 */
    @Excel(name = "下单价格")
    private BigDecimal orderPrice;

    /** 买卖方 */
    @Excel(name = "买卖方")
    private String side;

    /** 自定义订单信息 */
    @Excel(name = "自定义订单信息")
    private String text;

    /** 交易所订单ID */
    @Excel(name = "交易所订单ID")
    private String resultOrderId;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setUid(String uid) 
    {
        this.uid = uid;
    }

    public String getUid() 
    {
        return uid;
    }
    public void setChannelName(String channelName) 
    {
        this.channelName = channelName;
    }

    public String getChannelName() 
    {
        return channelName;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setMarket(String market) 
    {
        this.market = market;
    }

    public String getMarket() 
    {
        return market;
    }
    public void setQuoteAmt(BigDecimal quoteAmt) 
    {
        this.quoteAmt = quoteAmt;
    }

    public BigDecimal getQuoteAmt() 
    {
        return quoteAmt;
    }
    public void setTokenAmt(BigDecimal tokenAmt) 
    {
        this.tokenAmt = tokenAmt;
    }

    public BigDecimal getTokenAmt() 
    {
        return tokenAmt;
    }
    public void setOrderPrice(BigDecimal orderPrice) 
    {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getOrderPrice() 
    {
        return orderPrice;
    }
    public void setSide(String side) 
    {
        this.side = side;
    }

    public String getSide() 
    {
        return side;
    }
    public void setText(String text) 
    {
        this.text = text;
    }

    public String getText() 
    {
        return text;
    }
    public void setResultOrderId(String resultOrderId) 
    {
        this.resultOrderId = resultOrderId;
    }

    public String getResultOrderId() 
    {
        return resultOrderId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("uid", getUid())
            .append("channelName", getChannelName())
            .append("status", getStatus())
            .append("market", getMarket())
            .append("quoteAmt", getQuoteAmt())
            .append("tokenAmt", getTokenAmt())
            .append("orderPrice", getOrderPrice())
            .append("side", getSide())
            .append("text", getText())
            .append("resultOrderId", getResultOrderId())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
