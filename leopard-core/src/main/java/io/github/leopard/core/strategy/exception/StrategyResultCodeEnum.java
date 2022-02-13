/*
 * Copyright © 2020 pleuvoir (pleuvior@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.leopard.core.strategy.exception;

import lombok.Getter;

/**
 * 策略结果码
 *
 * @author <a href="mailto:fuwei@daojia-inc.com">pleuvoir</a>
 */
public enum StrategyResultCodeEnum {


    SUCCESS(0, "成功"),
    FAIL(1, "系统繁忙，请稍后再试。"),
    LACK_PARAM(4, "缺少参数"),
    PARAM_ERROR(5, "参数错误"),
    NOT_FOUND(6, "未找到该策略"),


    ACCOUNT_NOT_ENOUGH(7, "账户余额不足"),
    SPOT_ORDER_FAIL(8, "现货下单失败"),

    ;

    @Getter
    private Integer code;

    @Getter
    private String msg;

    private StrategyResultCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 若参数是字符串，判断字符串是否与code相同<br> 若参数是ResultCode对象，判断其中的code是否相同
     */
    public boolean isEquals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof String) {
            return this.getCode().equals(obj);
        } else if (obj instanceof StrategyResultCodeEnum) {
            return this.getCode().equals(((StrategyResultCodeEnum) obj).getCode());
        }
        return false;
    }

}
