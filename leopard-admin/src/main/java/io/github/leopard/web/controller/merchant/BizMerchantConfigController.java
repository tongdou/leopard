package io.github.leopard.web.controller.merchant;

import io.github.leopard.common.annotation.Log;
import io.github.leopard.common.core.controller.BaseController;
import io.github.leopard.common.core.domain.AjaxResult;
import io.github.leopard.common.core.page.TableDataInfo;
import io.github.leopard.common.enums.BusinessType;
import io.github.leopard.common.utils.ShiroUtils;
import io.github.leopard.common.utils.poi.ExcelUtil;
import io.github.leopard.system.domain.BizMerchantConfig;
import io.github.leopard.system.service.IBizMerchantConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商户信息配置Controller
 * 
 * @author liuxin
 * @date 2022-02-12
 */
@Controller
@RequestMapping("/merchant/config")
public class BizMerchantConfigController extends BaseController
{
    private String prefix = "merchant/config";

    @Autowired
    private IBizMerchantConfigService bizMerchantConfigService;

    @RequiresPermissions("merchant:config:view")
    @GetMapping()
    public String config()
    {
        return prefix + "/config";
    }

    /**
     * 查询商户信息配置列表
     */
    @RequiresPermissions("merchant:config:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizMerchantConfig bizMerchantConfig)
    {
        startPage();
        bizMerchantConfig.setUid(ShiroUtils.getUserId());
        List<BizMerchantConfig> list = bizMerchantConfigService.selectBizMerchantConfigList(bizMerchantConfig);
        return getDataTable(list);
    }

    /**
     * 导出商户信息配置列表
     */
    @RequiresPermissions("merchant:config:export")
    @Log(title = "商户信息配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizMerchantConfig bizMerchantConfig)
    {
        List<BizMerchantConfig> list = bizMerchantConfigService.selectBizMerchantConfigList(bizMerchantConfig);
        ExcelUtil<BizMerchantConfig> util = new ExcelUtil<BizMerchantConfig>(BizMerchantConfig.class);
        return util.exportExcel(list, "商户信息配置数据");
    }

    /**
     * 新增商户信息配置
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存商户信息配置
     */
    @RequiresPermissions("merchant:config:add")
    @Log(title = "商户信息配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizMerchantConfig bizMerchantConfig)
    {
        bizMerchantConfig.setUid(ShiroUtils.getUserId());
        return toAjax(bizMerchantConfigService.insertBizMerchantConfig(bizMerchantConfig));
    }

    /**
     * 修改商户信息配置
     */
    @RequiresPermissions("merchant:config:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        BizMerchantConfig bizMerchantConfig = bizMerchantConfigService.selectBizMerchantConfigById(id);
        mmap.put("bizMerchantConfig", bizMerchantConfig);
        return prefix + "/edit";
    }

    /**
     * 修改保存商户信息配置
     */
    @RequiresPermissions("merchant:config:edit")
    @Log(title = "商户信息配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizMerchantConfig bizMerchantConfig)
    {
        return toAjax(bizMerchantConfigService.updateBizMerchantConfig(bizMerchantConfig));
    }

    /**
     * 删除商户信息配置
     */
    @RequiresPermissions("merchant:config:remove")
    @Log(title = "商户信息配置", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(bizMerchantConfigService.deleteBizMerchantConfigByIds(ids));
    }
}
