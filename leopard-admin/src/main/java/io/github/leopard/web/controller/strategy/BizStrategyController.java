package io.github.leopard.web.controller.strategy;

import io.github.leopard.common.annotation.Log;
import io.github.leopard.common.core.controller.BaseController;
import io.github.leopard.common.core.domain.AjaxResult;
import io.github.leopard.common.core.page.TableDataInfo;
import io.github.leopard.common.enums.BusinessType;
import io.github.leopard.system.domain.BizStrategy;
import io.github.leopard.system.service.IBizStrategyService;
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
 * 策略Controller
 * 
 * @author admin
 * @date 2022-02-12
 */
@Controller
@RequestMapping("/strategy/base")
public class BizStrategyController extends BaseController
{
    private String prefix = "strategy/base";

    @Autowired
    private IBizStrategyService bizStrategyService;

    @GetMapping()
    public String strategy()
    {
        return prefix + "/strategy";
    }

    /**
     * 查询策略列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizStrategy bizStrategy)
    {
        startPage();
        List<BizStrategy> list = bizStrategyService.selectBizStrategyList(bizStrategy);
        return getDataTable(list);
    }


    /**
     * 新增策略
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存策略
     */
    @Log(title = "策略", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizStrategy bizStrategy)
    {
        return toAjax(bizStrategyService.insertBizStrategy(bizStrategy));
    }

    /**
     * 修改策略
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        BizStrategy bizStrategy = bizStrategyService.selectBizStrategyById(id);
        mmap.put("bizStrategy", bizStrategy);
        return prefix + "/edit";
    }

    /**
     * 修改保存策略
     */
    @Log(title = "策略", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizStrategy bizStrategy)
    {
        return toAjax(bizStrategyService.updateBizStrategy(bizStrategy));
    }

    /**
     * 删除策略
     */
    @Log(title = "策略", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(bizStrategyService.deleteBizStrategyByIds(ids));
    }
}
