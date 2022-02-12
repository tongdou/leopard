package io.github.leopard.system.domain;

import com.alibaba.fastjson.JSON;
import io.github.leopard.common.annotation.Excel;
import io.github.leopard.common.core.domain.BaseEntity;

/**
 * 商户信息配置对象 biz_merchant_config
 * 
 * @author liuxin
 * @date 2022-02-12
 */
public class BizMerchantConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long uid;

    /** key */
    @Excel(name = "key")
    private String gateApiKey;

    /** secret */
    @Excel(name = "secret")
    private String gateSecret;

    /** 用户微信uid */
    @Excel(name = "用户微信uid")
    private String wxUid;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private String isDeleted;

    public void setId(Long id) 
    {
        this.id = id;
    }

    /**
     * 生成的二维码地址
     */
    private String qrCodeUrl;

    public Long getId() 
    {
        return id;
    }
    public void setUid(Long uid) 
    {
        this.uid = uid;
    }

    public Long getUid() 
    {
        return uid;
    }
    public void setGateApiKey(String gateApiKey) 
    {
        this.gateApiKey = gateApiKey;
    }

    public String getGateApiKey() 
    {
        return gateApiKey;
    }
    public void setGateSecret(String gateSecret) 
    {
        this.gateSecret = gateSecret;
    }

    public String getGateSecret() 
    {
        return gateSecret;
    }
    public void setWxUid(String wxUid) 
    {
        this.wxUid = wxUid;
    }

    public String getWxUid() 
    {
        return wxUid;
    }
    public void setIsDeleted(String isDeleted) 
    {
        this.isDeleted = isDeleted;
    }

    public String getIsDeleted() 
    {
        return isDeleted;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
