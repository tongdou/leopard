package io.github.leopard.system.service.impl;

import java.util.List;

import io.github.leopard.common.exception.job.TaskException;
import io.github.leopard.common.utils.DateUtils;
import io.github.leopard.quartz.domain.SysJob;
import io.github.leopard.quartz.service.ISysJobService;
import org.quartz.SchedulerException;
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
    @Autowired
    private ISysJobService jobService;


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
    public int insertBizStrategyUser(BizStrategyUser bizStrategyUser) throws SchedulerException, TaskException {
        int i = bizStrategyUserMapper.insertBizStrategyUser(bizStrategyUser);
        bizStrategyUser.setCreateTime(DateUtils.getNowDate());
        //添加一个定时任务
        SysJob job  = new SysJob();
        job.setJobName(bizStrategyUser.getStrategyName());
        job.setJobGroup("DEFAULT");
        //corn表达式
        job.setCronExpression(bizStrategyUser.getCronExpression());
        //调用目标字符串 -- 执行方法
        String invokeTarget ="strategyExecutors.execute('"+bizStrategyUser.getId()+"')";
        job.setInvokeTarget(invokeTarget);
        //计划策略计划策略
        job.setMisfirePolicy("2");
        //状态
        job.setStatus("0");
        //允许并发
        job.setConcurrent("0");
        jobService.insertJob(job);
        return i;
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
