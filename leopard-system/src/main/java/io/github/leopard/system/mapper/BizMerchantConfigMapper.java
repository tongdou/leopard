package io.github.leopard.system.mapper;

import io.github.leopard.system.domain.BizMerchantConfig;

import java.util.List;

/**
 * 商户信息配置Mapper接口
 * 
 * @author liuxin
 * @date 2022-02-12
 */
public interface BizMerchantConfigMapper 
{
    /**
     * 查询商户信息配置
     * 
     * @param id 商户信息配置主键
     * @return 商户信息配置
     */
    public BizMerchantConfig selectBizMerchantConfigById(Long id);

    /**
     * 查询商户信息配置列表
     * 
     * @param bizMerchantConfig 商户信息配置
     * @return 商户信息配置集合
     */
    public List<BizMerchantConfig> selectBizMerchantConfigList(BizMerchantConfig bizMerchantConfig);

    /**
     * 新增商户信息配置
     * 
     * @param bizMerchantConfig 商户信息配置
     * @return 结果
     */
    public int insertBizMerchantConfig(BizMerchantConfig bizMerchantConfig);

    /**
     * 修改商户信息配置
     * 
     * @param bizMerchantConfig 商户信息配置
     * @return 结果
     */
    public int updateBizMerchantConfig(BizMerchantConfig bizMerchantConfig);

    /**
     * 删除商户信息配置
     * 
     * @param id 商户信息配置主键
     * @return 结果
     */
    public int deleteBizMerchantConfigById(Long id);

    /**
     * 批量删除商户信息配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizMerchantConfigByIds(String[] ids);
}
