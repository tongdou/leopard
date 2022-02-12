package io.github.leopard.web.controller.other;

import com.alibaba.fastjson.JSON;
import com.zjiecode.wxpusher.client.bean.callback.AppSubscribeBean;
import com.zjiecode.wxpusher.client.bean.callback.BaseCallBackReq;
import io.github.leopard.common.utils.LogUtils;
import io.github.leopard.system.service.IBizMerchantConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * WxPusher微信推送服务
 *
 * @author meteor
 */
@RestController
@RequestMapping("/wxpusher")
public class WxPusherController {

    @Autowired
    private IBizMerchantConfigService bizMerchantConfigService;

    /**
     * 微信回调通知
     */
    @PostMapping("/callback")
    public void wxpusherCallback(@RequestBody BaseCallBackReq baseCallBackReq, HttpServletRequest request) {
        LogUtils.INFO_LOG.info("WxPusher微信推送服务回调参数:{}", JSON.toJSONString(baseCallBackReq));
        if (baseCallBackReq != null) {
            String action = baseCallBackReq.getAction();
            if (StringUtils.isNotBlank(action)) {
                //关注公众号的回调
                if (action.equals(BaseCallBackReq.ACTION_APP_SUBSCRIBE)) {
                    AppSubscribeBean data = JSON.parseObject(JSON.toJSONString(baseCallBackReq.getData()),AppSubscribeBean.class);
                    String leopardUid = data.getExtra();
                    String wxUid = data.getUid();

                    bizMerchantConfigService.updateWxUidByLeopardUid(leopardUid, wxUid);
                }
                //TODO 其它指令回调send_up_cmd
            }
        }
    }
}
