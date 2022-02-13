package io.github.leopard.core.strategy.track;

import io.github.leopard.common.enums.PercentChangeEnums;
import io.github.leopard.core.strategy.StrategyParam;
import io.github.leopard.core.strategy.exception.StrategyExecuteException;
import io.github.leopard.core.strategy.impl.AbstractStrategy;
import io.github.leopard.exchange.client.IExchangeApi;
import io.github.leopard.exchange.extension.GateApiExtension;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * K线监控,然后执行响应的操作
 * T 计算时返回的对象
 * S 配置的参数转换为的对象
 * U 交易返回的对象
 * @description:
 * @author: liuxin79
 * @date: 2022-02-10 11:28
 */
public abstract class AbstractTrackCandlestickStrategy<T,S,U> extends AbstractStrategy {

    /**
     * 执行k线监控
     */
    @Override
    public void execute(IExchangeApi exchangeApi, StrategyParam<String, String> param) throws StrategyExecuteException {
        GateApiExtension api = (GateApiExtension) exchangeApi;
        // 1.构造监控的参数,监控周期,振幅比例
        S monitoringParam = buildMonitoringParam(param);
        // 2.查询需要监控的币种
        List<String> currencyPriceList = fetchCurrencyPriceList(api,monitoringParam);
        if(CollectionUtils.isEmpty(currencyPriceList)){
            return;
        }
        //并行处理
        currencyPriceList.parallelStream().forEach(currency -> {
            try {
                // 3.计算监控结果
                T dataResult = computeMonitoringResult(monitoringParam, currency);
                //计算结果
                if (null == dataResult) {
                    return;
                }
                // 特殊操作
                Boolean flag = specialProcess(dataResult,api);
                if (!flag) {
                    return;
                }
                // 4.交易操作
               U transaction= transactionProcess(dataResult,api);
                // 5.构造消息通知
                String contentMsg = buildWxMessage(dataResult,transaction);
                if (StringUtils.isEmpty(contentMsg)) {
                    return;
                }
                // 6.发送消息
                sendWxMessage(contentMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 构造监控的参数,监控周期,振幅比例
     * @param param
     * @return
     */
    protected abstract S buildMonitoringParam(StrategyParam<String, String> param);

    /**
     * 获取监控币种信息
     * @param api
     * @param monitoringParam
     * @return
     */
    protected abstract List<String> fetchCurrencyPriceList(GateApiExtension api ,S monitoringParam);


    /**
     * 计算监控结果
     *
     * @param monitoringParam
     * @param currency 市场币种
     * @return
     */
    protected abstract T computeMonitoringResult( S monitoringParam, String currency);

    /**
     * 特殊操作
     * @param dataResult
     * @param api
     * @return
     */
    protected Boolean specialProcess(T dataResult,GateApiExtension api) {
        return Boolean.TRUE;
    }

    /**
     * 交易流程
     * @param dataResult
     * @param api
     * @return
     */
    protected abstract U transactionProcess(T dataResult,GateApiExtension api);


    /**
     * 发送消息
     *
     * @param dataResult
     * @return
     */
    protected abstract String buildWxMessage(T dataResult,U transaction);

    /**
     * 发送消息
     *
     * @param contentMsg
     */
    @Async
    protected abstract void sendWxMessage(String contentMsg);


}
