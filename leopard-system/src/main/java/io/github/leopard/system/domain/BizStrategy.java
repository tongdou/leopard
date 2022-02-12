package io.github.leopard.system.domain;

import io.github.leopard.common.annotation.Excel;
import io.github.leopard.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 策略对象 biz_strategy
 * 
 * @author admin
 * @date 2022-02-12
 */
public class BizStrategy extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 策略名称 */
    @Excel(name = "策略名称")
    private String strategyName;

    /** beanName */
    @Excel(name = "beanName")
    private String beanName;

    /** className */
    @Excel(name = "className")
    private String className;

    /** 自定义配置 */
    @Excel(name = "自定义配置")
    private String configJson;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setStrategyName(String strategyName) 
    {
        this.strategyName = strategyName;
    }

    public String getStrategyName() 
    {
        return strategyName;
    }
    public void setBeanName(String beanName) 
    {
        this.beanName = beanName;
    }

    public String getBeanName() 
    {
        return beanName;
    }
    public void setClassName(String className) 
    {
        this.className = className;
    }

    public String getClassName() 
    {
        return className;
    }
    public void setConfigJson(String configJson) 
    {
        this.configJson = configJson;
    }

    public String getConfigJson() 
    {
        return configJson;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("strategyName", getStrategyName())
            .append("beanName", getBeanName())
            .append("className", getClassName())
            .append("configJson", getConfigJson())
            .append("createBy", getCreateBy())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
