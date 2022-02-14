package io.github.leopard.core.handler;

import io.github.leopard.common.utils.WechatNotifyUtils;
import io.github.leopard.system.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 发送微信消息
 * @author meteor
 */
@Service
public class WxPusherHandler {

    @Autowired
    private DictService dictService;

    /**
     * @param wxuid
     * @param content
     */
    public  void sendMessage(String wxuid, String content){
        //TODO 加入缓存
        String appToken = dictService.getLabel("leopard_wx_pusher", "WXPUSHER_APP_TOKEN");
        WechatNotifyUtils.send(appToken,wxuid,content);
    }
}
