package io.github.leopard.core.strategy;

/*
 * 策略异常
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
import lombok.Getter;

public class StrategyException extends Exception {

    private static final long serialVersionUID = -121219158129626814L;

    @Getter
    private StrategyResultCodeEnum resultCode;
    @Getter
    private String msg;

    public StrategyException() {
    }

    public StrategyException(StrategyResultCodeEnum rsCode) {
        super(rsCode.getCode() + ":" + rsCode.getMsg());
        this.resultCode = rsCode;
        this.msg = rsCode.getMsg();
    }

    public StrategyException(StrategyResultCodeEnum rsCode, String message) {
        super(rsCode.getCode() + ":" + message);
        this.resultCode = rsCode;
        this.msg = message;
    }

    public StrategyException(StrategyResultCodeEnum rsCode, Throwable cause) {
        super(rsCode.getCode() + ":" + rsCode.getMsg(), cause);
        this.resultCode = rsCode;
        this.msg = rsCode.getMsg();
    }

    public StrategyException(StrategyResultCodeEnum rsCode, String message, Throwable cause) {
        super(rsCode.getCode() + ":" + message, cause);
        this.resultCode = rsCode;
        this.msg = message;
    }
}
