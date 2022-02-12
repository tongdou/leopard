package io.github.leopard.system.service.impl;

import java.util.List;
import io.github.leopard.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.leopard.system.mapper.BizStrategyUserMapper;
import io.github.leopard.system.domain.BizStrategyUser;
import io.github.leopard.system.service.IBizStrategyUserService;
import io.github.leopard.common.core.text.Convert;

/**
 * 用户策略Service业务层处理
 * 
 * @author admin
 * @date 2022-02-12
 */
@Service
public class BizStrategyUserServiceImpl implements IBizStrategyUserService 
{
    @Autowired
    private BizStrategyUserMapper bizStrategyUserMapper;

    /**
     * 查询用户策略
     * 
     * @param id 用户策略主键
     * @return 用户策略
     */
    @Override
    public BizStrategyUser selectBizStrategyUserById(String id)
    {
        return bizStrategyUserMapper.selectBizStrategyUserById(id);
    }

    /**
     * 查询用户策略列表
     * 
     * @param bizStrategyUser 用户策略
     * @return 用户策略
     */
    @Override
    public List<BizStrategyUser> selectBizStrategyUserList(BizStrategyUser bizStrategyUser)
    {
        return bizStrategyUserMapper.selectBizStrategyUserList(bizStrategyUser);
    }

    /**
     * 新增用户策略
     * 
     * @param bizStrategyUser 用户策略
     * @return 结果
     */
    @Override
    public int insertBizStrategyUser(BizStrategyUser bizStrategyUser)
    {
        bizStrategyUser.setCreateTime(DateUtils.getNowDate());
        return bizStrategyUserMapper.insertBizStrategyUser(bizStrategyUser);
    }

    /**
     * 修改用户策略
     * 
     * @param bizStrategyUser 用户策略
     * @return 结果
     */
    @Override
    public int updateBizStrategyUser(BizStrategyUser bizStrategyUser)
    {
        bizStrategyUser.setUpdateTime(DateUtils.getNowDate());
        return bizStrategyUserMapper.updateBizStrategyUser(bizStrategyUser);
    }

    /**
     * 批量删除用户策略
     * 
     * @param ids 需要删除的用户策略主键
     * @return 结果
     */
    @Override
    public int deleteBizStrategyUserByIds(String ids)
    {
        return bizStrategyUserMapper.deleteBizStrategyUserByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除用户策略信息
     * 
     * @param id 用户策略主键
     * @return 结果
     */
    @Override
    public int deleteBizStrategyUserById(String id)
    {
        return bizStrategyUserMapper.deleteBizStrategyUserById(id);
    }
}
