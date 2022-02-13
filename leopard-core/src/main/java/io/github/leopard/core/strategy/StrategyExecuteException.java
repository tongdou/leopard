package io.github.leopard.core.strategy;

/*
 * 策略执行异常
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */

import lombok.Getter;

public class StrategyExecuteException extends Exception {

    private static final long serialVersionUID = -121219158129626814L;

    @Getter
    private StrategyResultCodeEnum resultCode;
    @Getter
    private String msg;

    public StrategyExecuteException() {
    }

    public StrategyExecuteException(StrategyResultCodeEnum rsCode) {
        super(rsCode.getCode() + ":" + rsCode.getMsg());
        this.resultCode = rsCode;
        this.msg = rsCode.getMsg();
    }

    public StrategyExecuteException(StrategyResultCodeEnum rsCode, String message) {
        super(rsCode.getCode() + ":" + message);
        this.resultCode = rsCode;
        this.msg = message;
    }

    public StrategyExecuteException(StrategyResultCodeEnum rsCode, Throwable cause) {
        super(rsCode.getCode() + ":" + rsCode.getMsg(), cause);
        this.resultCode = rsCode;
        this.msg = rsCode.getMsg();
    }

    public StrategyExecuteException(StrategyResultCodeEnum rsCode, String message, Throwable cause) {
        super(rsCode.getCode() + ":" + message, cause);
        this.resultCode = rsCode;
        this.msg = message;
    }
}
