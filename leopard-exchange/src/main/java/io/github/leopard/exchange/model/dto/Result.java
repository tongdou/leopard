package io.github.leopard.exchange.model.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.github.leopard.exchange.exception.ExchangeResultCodeEnum;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 返回结果
 *
 * @author <a href="mailto:fuwei@daojia-inc.com">pleuvoir</a>
 */
@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 7781316982699830573L;

    private Integer code;//返回码

    private String msg;//返回信息

    private T data;     //数据

    public Result() {
    }

    public Result(ExchangeResultCodeEnum rspCode) {
        this.code = rspCode.getCode();
        this.msg = rspCode.getMsg();
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(ExchangeResultCodeEnum rspCode, String msg) {
        this.code = rspCode.getCode();
        this.msg = msg;
    }

    public Result<T> setResult(ExchangeResultCodeEnum rspCode) {
        this.code = rspCode.getCode();
        this.msg = rspCode.getMsg();
        return this;
    }

    public Result<T> setResult(ExchangeResultCodeEnum rspCode, String msg) {
        this.code = rspCode.getCode();
        this.msg = msg;
        return this;
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean isSuccess() {
        return ExchangeResultCodeEnum.SUCCESS.isEquals(code);
    }

    public static <T> Result<T> success() {
        return new Result<>(ExchangeResultCodeEnum.SUCCESS, ExchangeResultCodeEnum.SUCCESS.getMsg());
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>(ExchangeResultCodeEnum.SUCCESS, ExchangeResultCodeEnum.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(ExchangeResultCodeEnum.FAIL, message);
    }

    public static <T> Result<T> fail(ExchangeResultCodeEnum rspCode, String message) {
        return new Result<>(rspCode, message);
    }

    public static <T> Result<T> fail(ExchangeResultCodeEnum rspCode) {
        return new Result<>(rspCode, rspCode.getMsg());
    }
}
