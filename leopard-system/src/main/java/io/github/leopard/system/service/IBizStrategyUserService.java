package io.github.leopard.system.service;

import java.util.List;
import io.github.leopard.system.domain.BizStrategyUser;

/**
 * 用户策略Service接口
 * 
 * @author admin
 * @date 2022-02-12
 */
public interface IBizStrategyUserService 
{
    /**
     * 查询用户策略
     * 
     * @param id 用户策略主键
     * @return 用户策略
     */
    public BizStrategyUser selectBizStrategyUserById(String id);

    /**
     * 查询用户策略列表
     * 
     * @param bizStrategyUser 用户策略
     * @return 用户策略集合
     */
    public List<BizStrategyUser> selectBizStrategyUserList(BizStrategyUser bizStrategyUser);

    /**
     * 新增用户策略
     * 
     * @param bizStrategyUser 用户策略
     * @return 结果
     */
    public int insertBizStrategyUser(BizStrategyUser bizStrategyUser);

    /**
     * 修改用户策略
     * 
     * @param bizStrategyUser 用户策略
     * @return 结果
     */
    public int updateBizStrategyUser(BizStrategyUser bizStrategyUser);

    /**
     * 批量删除用户策略
     * 
     * @param ids 需要删除的用户策略主键集合
     * @return 结果
     */
    public int deleteBizStrategyUserByIds(String ids);

    /**
     * 删除用户策略信息
     * 
     * @param id 用户策略主键
     * @return 结果
     */
    public int deleteBizStrategyUserById(String id);
}
