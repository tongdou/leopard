package io.github.leopard.system.service.impl;

import io.github.leopard.common.core.text.Convert;
import io.github.leopard.common.utils.DateUtils;
import io.github.leopard.system.domain.BizMerchantConfig;
import io.github.leopard.system.mapper.BizMerchantConfigMapper;
import io.github.leopard.system.service.IBizMerchantConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商户信息配置Service业务层处理
 * 
 * @author liuxin
 * @date 2022-02-12
 */
@Service
public class BizMerchantConfigServiceImpl implements IBizMerchantConfigService
{
    @Autowired
    private BizMerchantConfigMapper bizMerchantConfigMapper;

    /**
     * 查询商户信息配置
     * 
     * @param id 商户信息配置主键
     * @return 商户信息配置
     */
    @Override
    public BizMerchantConfig selectBizMerchantConfigById(Long id)
    {
        return bizMerchantConfigMapper.selectBizMerchantConfigById(id);
    }

    /**
     * 查询商户信息配置列表
     * 
     * @param bizMerchantConfig 商户信息配置
     * @return 商户信息配置
     */
    @Override
    public List<BizMerchantConfig> selectBizMerchantConfigList(BizMerchantConfig bizMerchantConfig)
    {
        return bizMerchantConfigMapper.selectBizMerchantConfigList(bizMerchantConfig);
    }

    /**
     * 新增商户信息配置
     * 
     * @param bizMerchantConfig 商户信息配置
     * @return 结果
     */
    @Override
    public int insertBizMerchantConfig(BizMerchantConfig bizMerchantConfig)
    {
        bizMerchantConfig.setCreateTime(DateUtils.getNowDate());
        return bizMerchantConfigMapper.insertBizMerchantConfig(bizMerchantConfig);
    }

    /**
     * 修改商户信息配置
     * 
     * @param bizMerchantConfig 商户信息配置
     * @return 结果
     */
    @Override
    public int updateBizMerchantConfig(BizMerchantConfig bizMerchantConfig)
    {
        bizMerchantConfig.setUpdateTime(DateUtils.getNowDate());
        return bizMerchantConfigMapper.updateBizMerchantConfig(bizMerchantConfig);
    }

    /**
     * 批量删除商户信息配置
     * 
     * @param ids 需要删除的商户信息配置主键
     * @return 结果
     */
    @Override
    public int deleteBizMerchantConfigByIds(String ids)
    {
        return bizMerchantConfigMapper.deleteBizMerchantConfigByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商户信息配置信息
     * 
     * @param id 商户信息配置主键
     * @return 结果
     */
    @Override
    public int deleteBizMerchantConfigById(Long id)
    {
        return bizMerchantConfigMapper.deleteBizMerchantConfigById(id);
    }
}
