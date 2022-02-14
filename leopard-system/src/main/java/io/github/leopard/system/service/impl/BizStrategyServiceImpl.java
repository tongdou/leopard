package io.github.leopard.system.service.impl;

import java.util.List;
import io.github.leopard.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.leopard.system.mapper.BizStrategyMapper;
import io.github.leopard.system.domain.BizStrategy;
import io.github.leopard.system.service.IBizStrategyService;
import io.github.leopard.common.core.text.Convert;

/**
 * 策略Service业务层处理
 * 
 * @author admin
 * @date 2022-02-12
 */
@Service(value = "bizStrategyService")
public class BizStrategyServiceImpl implements IBizStrategyService 
{
    @Autowired
    private BizStrategyMapper bizStrategyMapper;

    /**
     * 查询策略
     * 
     * @param id 策略主键
     * @return 策略
     */
    @Override
    public BizStrategy selectBizStrategyById(String id)
    {
        return bizStrategyMapper.selectBizStrategyById(id);
    }

    /**
     * 查询策略列表
     * 
     * @param bizStrategy 策略
     * @return 策略
     */
    @Override
    public List<BizStrategy> selectBizStrategyList(BizStrategy bizStrategy)
    {
        return bizStrategyMapper.selectBizStrategyList(bizStrategy);
    }

    @Override
    public List<BizStrategy> selectAllBizStrategyList() {
        return bizStrategyMapper.selectBizStrategyList(new BizStrategy());
    }

    /**
     * 新增策略
     * 
     * @param bizStrategy 策略
     * @return 结果
     */
    @Override
    public int insertBizStrategy(BizStrategy bizStrategy)
    {
        bizStrategy.setCreateTime(DateUtils.getNowDate());
        return bizStrategyMapper.insertBizStrategy(bizStrategy);
    }

    /**
     * 修改策略
     * 
     * @param bizStrategy 策略
     * @return 结果
     */
    @Override
    public int updateBizStrategy(BizStrategy bizStrategy)
    {
        bizStrategy.setUpdateTime(DateUtils.getNowDate());
        return bizStrategyMapper.updateBizStrategy(bizStrategy);
    }

    /**
     * 批量删除策略
     * 
     * @param ids 需要删除的策略主键
     * @return 结果
     */
    @Override
    public int deleteBizStrategyByIds(String ids)
    {
        return bizStrategyMapper.deleteBizStrategyByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除策略信息
     * 
     * @param id 策略主键
     * @return 结果
     */
    @Override
    public int deleteBizStrategyById(String id)
    {
        return bizStrategyMapper.deleteBizStrategyById(id);
    }
}
