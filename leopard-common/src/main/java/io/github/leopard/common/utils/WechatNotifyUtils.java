package io.github.leopard.common.utils;

import com.alibaba.fastjson.JSON;
import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.Message;
import com.zjiecode.wxpusher.client.bean.MessageResult;
import com.zjiecode.wxpusher.client.bean.Result;

import java.util.List;

/**
 * 微信通知消息
 * @author meteor
 */

public class WechatNotifyUtils {

    public static void send(String appToken,String uid,String text) {
        try {
            Message message = new Message();
            message.setAppToken(appToken);
            message.setContentType(Message.CONTENT_TYPE_MD);
            message.setContent(text);
            message.setUid(uid);
            Result<List<MessageResult>> result = WxPusher.send(message);
            LogUtils.INFO_LOG.info("通知发送。result={}", JSON.toJSON(result));
        } catch (Throwable e) {
            LogUtils.INFO_LOG.warn("消息推送失败，text={}", text, e);
        }
    }
}
