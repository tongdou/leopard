package io.github.leopard.exchange.exception;

import com.alibaba.fastjson.JSON;
import io.gate.gateapi.ApiException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * 异常转换
 * <p>
 * https://www.gateio.tv/docs/developers/apiv4/zh_CN/
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Slf4j
public class ExchangeExceptionTranslator {


    public static void transform(Throwable e, String func, String requestJson) throws ExchangeApiException {
        if (e instanceof ApiException) {
            ApiException ae = (ApiException) e;
            int aecode = ae.getCode();
            String aeMessage = ae.getMessage();
            log.warn("[{}]]异常，aecode={}，message={}，requestJson={}", func, aecode, aeMessage, requestJson);

            if (ae.getCause() instanceof IOException) {
                throw new ExchangeApiException(ExchangeResultCodeEnum.TIMEOUT);
            }
            if (aecode == 400) {
                throw new ExchangeApiException(ExchangeResultCodeEnum.PARAM_ERROR, aeMessage);
            } else if (aecode == 404 && e.getMessage().contains("ORDER_NOT_FOUND")) {
                throw new ExchangeApiException(ExchangeResultCodeEnum.ORDER_NOT_FOUND);
            } else {
                throw new ExchangeApiException(ExchangeResultCodeEnum.FAIL);
            }
        } else {
            log.error("[{}]]exchange api  未知的异常，requestJson={}", func, requestJson, e);
            throw new ExchangeApiException(ExchangeResultCodeEnum.FAIL);
        }
    }
}
