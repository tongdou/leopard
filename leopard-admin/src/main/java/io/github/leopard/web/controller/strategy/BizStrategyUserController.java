package io.github.leopard.web.controller.strategy;

import io.github.leopard.common.annotation.Log;
import io.github.leopard.common.core.controller.BaseController;
import io.github.leopard.common.core.domain.AjaxResult;
import io.github.leopard.common.core.page.TableDataInfo;
import io.github.leopard.common.enums.BusinessType;
import io.github.leopard.system.domain.BizStrategyUser;
import io.github.leopard.system.service.IBizStrategyUserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户策略Controller
 * 
 * @author admin
 * @date 2022-02-12
 */
@Controller
@RequestMapping("/strategy/user")
public class BizStrategyUserController extends BaseController
{
    private String prefix = "strategy/user";

    @Autowired
    private IBizStrategyUserService bizStrategyUserService;

    @GetMapping()
    public String strategy()
    {
        return prefix + "/strategy";
    }

    /**
     * 查询用户策略列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizStrategyUser bizStrategyUser)
    {
        startPage();
        List<BizStrategyUser> list = bizStrategyUserService.selectBizStrategyUserList(bizStrategyUser);
        return getDataTable(list);
    }


    /**
     * 新增用户策略
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存用户策略
     */
    @Log(title = "用户策略", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizStrategyUser bizStrategyUser)
    {
        return toAjax(bizStrategyUserService.insertBizStrategyUser(bizStrategyUser));
    }

    /**
     * 修改用户策略
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        BizStrategyUser bizStrategyUser = bizStrategyUserService.selectBizStrategyUserById(id);
        mmap.put("bizStrategyUser", bizStrategyUser);
        return prefix + "/edit";
    }

    /**
     * 修改保存用户策略
     */
    @Log(title = "用户策略", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizStrategyUser bizStrategyUser)
    {
        return toAjax(bizStrategyUserService.updateBizStrategyUser(bizStrategyUser));
    }

    /**
     * 删除用户策略
     */
    @Log(title = "用户策略", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(bizStrategyUserService.deleteBizStrategyUserByIds(ids));
    }
}
