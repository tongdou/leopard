package io.github.leopard.exchange.exception;

/*
 * 交易所异常
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */

import lombok.Getter;

public class ExchangeApiException extends Exception {

    private static final long serialVersionUID = -121219158129626814L;

    @Getter
    private ExchangeResultCodeEnum resultCode;
    @Getter
    private String msg;

    public ExchangeApiException() {
    }

    public ExchangeApiException(ExchangeResultCodeEnum rsCode) {
        super(rsCode.getCode() + ":" + rsCode.getMsg());
        this.resultCode = rsCode;
        this.msg = rsCode.getMsg();
    }

    public ExchangeApiException(ExchangeResultCodeEnum rsCode, String message) {
        super(rsCode.getCode() + ":" + message);
        this.resultCode = rsCode;
        this.msg = message;
    }

    public ExchangeApiException(ExchangeResultCodeEnum rsCode, Throwable cause) {
        super(rsCode.getCode() + ":" + rsCode.getMsg(), cause);
        this.resultCode = rsCode;
        this.msg = rsCode.getMsg();
    }

    public ExchangeApiException(ExchangeResultCodeEnum rsCode, String message, Throwable cause) {
        super(rsCode.getCode() + ":" + message, cause);
        this.resultCode = rsCode;
        this.msg = message;
    }
}
