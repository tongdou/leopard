package io.github.leopard.system.domain;

import io.github.leopard.common.annotation.Excel;
import io.github.leopard.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户策略对象 biz_strategy_user
 * 
 * @author admin
 * @date 2022-02-12
 */
public class BizStrategyUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** uid */
    private String uid;

    /** 策略id */
    @Excel(name = "策略id")
    private String strategyId;

    /** 策略名称 */
    @Excel(name = "策略名称")
    private String strategyName;

    /** 自定义配置 */
    @Excel(name = "自定义配置")
    private String configJson;

    /** 策略运行状态 */
    @Excel(name = "策略运行状态")
    private String status;

    /**
     * corn表达式
     */
    private String cronExpression;

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
    public void setStrategyId(String strategyId) 
    {
        this.strategyId = strategyId;
    }

    public String getStrategyId() 
    {
        return strategyId;
    }
    public void setStrategyName(String strategyName) 
    {
        this.strategyName = strategyName;
    }

    public String getStrategyName() 
    {
        return strategyName;
    }
    public void setConfigJson(String configJson) 
    {
        this.configJson = configJson;
    }

    public String getConfigJson() 
    {
        return configJson;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("uid", getUid())
            .append("strategyId", getStrategyId())
            .append("strategyName", getStrategyName())
            .append("configJson", getConfigJson())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }
}
