package io.github.leopard.web.controller.order;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import io.github.leopard.common.annotation.Log;
import io.github.leopard.common.enums.BusinessType;
import io.github.leopard.system.domain.BizOrder;
import io.github.leopard.system.service.IBizOrderService;
import io.github.leopard.common.core.controller.BaseController;
import io.github.leopard.common.core.domain.AjaxResult;
import io.github.leopard.common.utils.poi.ExcelUtil;
import io.github.leopard.common.core.page.TableDataInfo;

/**
 * 订单信息Controller
 * 
 * @author admin
 * @date 2022-02-12
 */
@Controller
@RequestMapping("/order/order")
public class BizOrderController extends BaseController
{
    private String prefix = "order/order";

    @Autowired
    private IBizOrderService bizOrderService;

    @GetMapping()
    public String order()
    {
        return prefix + "/order";
    }

    /**
     * 查询订单信息列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizOrder bizOrder)
    {
        startPage();
        List<BizOrder> list = bizOrderService.selectBizOrderList(bizOrder);

        Long userId = getUserId();

        List<BizOrder> result = list.stream().filter(k -> Long.valueOf(k.getUid()).equals(userId)).collect(Collectors.toList());

        return getDataTable(result);
    }

}
