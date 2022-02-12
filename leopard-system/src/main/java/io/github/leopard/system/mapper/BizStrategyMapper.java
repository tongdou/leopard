package io.github.leopard.system.mapper;

import java.util.List;
import io.github.leopard.system.domain.BizStrategy;

/**
 * 策略Mapper接口
 * 
 * @author admin
 * @date 2022-02-12
 */
public interface BizStrategyMapper 
{
    /**
     * 查询策略
     * 
     * @param id 策略主键
     * @return 策略
     */
    public BizStrategy selectBizStrategyById(String id);

    /**
     * 查询策略列表
     * 
     * @param bizStrategy 策略
     * @return 策略集合
     */
    public List<BizStrategy> selectBizStrategyList(BizStrategy bizStrategy);

    /**
     * 新增策略
     * 
     * @param bizStrategy 策略
     * @return 结果
     */
    public int insertBizStrategy(BizStrategy bizStrategy);

    /**
     * 修改策略
     * 
     * @param bizStrategy 策略
     * @return 结果
     */
    public int updateBizStrategy(BizStrategy bizStrategy);

    /**
     * 删除策略
     * 
     * @param id 策略主键
     * @return 结果
     */
    public int deleteBizStrategyById(String id);

    /**
     * 批量删除策略
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizStrategyByIds(String[] ids);
}
