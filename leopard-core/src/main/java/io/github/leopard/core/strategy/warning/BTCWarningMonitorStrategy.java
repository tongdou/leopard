package io.github.leopard.core.strategy.warning;

import io.github.leopard.common.constant.CurrencyConstants;
import io.github.leopard.common.utils.CurrencyUtils;
import io.github.leopard.common.utils.DateUtils;
import io.github.leopard.core.strategy.StrategyParam;
import io.github.leopard.core.strategy.model.MonitoringBaseResultDTO;
import io.github.leopard.core.strategy.model.SpecifyCurrencyMonitoringDTO;
import io.github.leopard.core.strategy.track.AbstractTrackCandlestickStrategy;
import io.github.leopard.exchange.extension.GateApiExtension;
import io.github.leopard.exchange.model.dto.request.CandlestickRequestDTO;
import io.github.leopard.exchange.model.dto.result.CandlestickResultDTO;
import io.github.leopard.exchange.model.dto.result.EatSpotOrderMarketResultDTO;
import io.github.leopard.exchange.model.dto.result.TickResultDTO;
import io.github.leopard.exchange.model.enums.CandlesticksIntervalEnum;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @description: BTC的预警监控, 短期内超过一定涨幅
 * @author: liuxin79
 * @date: 2022-02-10 11:55
 */
@Service(value = "BTCWarningMonitorStrategy")
public class BTCWarningMonitorStrategy extends AbstractTrackCandlestickStrategy<Map<String, String>,SpecifyCurrencyMonitoringDTO, EatSpotOrderMarketResultDTO> {

    /**
     * 构造监控的参数,监控周期,振幅比例
     *
     * @param param
     * @return
     */
    @Override
    protected Map<String, String> buildMonitoringParam(StrategyParam<String, String> param) {
        //获取当前的分钟
        Map<String, String> monitoringParamMap = new LinkedHashMap<>();
        Integer minute = DateUtils.getMinute();

        //1分钟线
        monitoringParamMap.put(param.getString("monitoring_cycle_1m"),param.getString("monitoring_ratio_1m"));
        //5分钟查一次的
        List<Integer> minute_5_List = Arrays.asList(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55);
        if (minute_5_List.contains(minute)) {
            monitoringParamMap.put(param.getString("monitoring_cycle_5m"), param.getString("monitoring_ratio_5m"));
        }
        //15分钟查一次的
        List<Integer> minute_15_List = Arrays.asList(15, 30, 45, 0);
        if (minute_15_List.contains(minute)) {
            monitoringParamMap.put(param.getString("monitoring_cycle_15m"), param.getString("monitoring_ratio_15m"));
        }
        //30分钟查一次的
        List<Integer> minute_30_List = Arrays.asList(30, 0);
        if (minute_30_List.contains(minute)) {
            monitoringParamMap.put(param.getString("monitoring_cycle_30m"), param.getString("monitoring_ratio_30m"));
        }
        //1小时查一次的
        List<Integer> hour_1h_List = Arrays.asList(0);
        if (hour_1h_List.contains(minute)) {
            monitoringParamMap.put(param.getString("monitoring_cycle_1h"), param.getString("monitoring_ratio_1h"));
        }
        //4小时查一次的
        int hour = DateUtils.getHour();
        List<Integer> hour_4h_List = Arrays.asList(0, 4, 8, 12, 16, 20);
        if (hour_4h_List.contains(hour) && hour_1h_List.contains(minute)) {
            monitoringParamMap.put(param.getString("monitoring_cycle_4h"), param.getString("monitoring_ratio_4h"));
        }
        return monitoringParamMap;
    }

    /**
     * 获取监控币种信息
     *
     * @param api
     * @param monitoringParam
     * @return
     */
    @Override
    protected List<String> fetchCurrencyPriceList(GateApiExtension api, Map<String, String> monitoringParam) {
        List<String> currencyList = new ArrayList<>();
        currencyList.add(CurrencyConstants.BTC_USDT);
        return currencyList;
    }

    /**
     * 计算监控结果
     *
     * @param monitoringParam
     * @param market        市场币种
     * @return
     */
    @Override
    protected SpecifyCurrencyMonitoringDTO computeMonitoringResult(Map<String, String> monitoringParam, String market) {
        //返回计算结果
        SpecifyCurrencyMonitoringDTO specifyCurrencyMonitoring = new SpecifyCurrencyMonitoringDTO();
        List<MonitoringBaseResultDTO> baseResultList = new LinkedList<>();
        GateApiExtension client = GateApiExtension.create();

        //获取当前交易对tick信息
        TickResultDTO tickerNow = client.getTickerMust(market);

        //循环监控数据
        monitoringParam.forEach((monitoringCycle, monitoringAmplitudeRatio) -> {
            MonitoringBaseResultDTO amplitudeResult = new MonitoringBaseResultDTO();
            // 当前周期的K线图数据
            CandlestickRequestDTO request = new CandlestickRequestDTO();
            request.setMarket(market);
            request.setIntervalEnum(CandlesticksIntervalEnum.toEnum(monitoringCycle));
            request.setLimit(1);
            List<CandlestickResultDTO> candlestickResultList = client.listCandlestickMust(request);
            if(CollectionUtils.isNotEmpty(candlestickResultList)){
                CandlestickResultDTO candlestickResult = candlestickResultList.get(0);
                //开盘价
                BigDecimal openPrice =candlestickResult.getOpen() ;
                //收盘价
                BigDecimal closePrice = candlestickResult.getClose();
                //收盘价-开盘价 = 差值
                BigDecimal differenceValue = closePrice.subtract(openPrice);
                //计算振幅比例, 差值/收盘价
                BigDecimal amplitudeRatioValue = differenceValue.divide(openPrice, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).abs();
                if (amplitudeRatioValue.compareTo(new BigDecimal(monitoringAmplitudeRatio)) >= 0) {
                    boolean isFall = differenceValue.compareTo(BigDecimal.ZERO) < 0;
                    //实际振幅比例
                    String amplitudeRatio = amplitudeRatioValue.setScale(2, RoundingMode.HALF_UP) + "%";
                    //差值的绝对值就是振幅金额
                    String amplitudeAmount = differenceValue.abs().stripTrailingZeros().toPlainString();
                    //赋值
                    amplitudeResult.setFall(isFall);
                    amplitudeResult.setAmplitudeAmount(amplitudeAmount);
                    amplitudeResult.setAmplitudeRatio(amplitudeRatio);
                    amplitudeResult.setMonitoringCycle(monitoringCycle);
                    baseResultList.add(amplitudeResult);
                }
            }
        });
        specifyCurrencyMonitoring.setCurrency(market);
        //当前价格
        BigDecimal currentPrice = tickerNow.getLast();
        specifyCurrencyMonitoring.setCurrentPrice(currentPrice);
        //今日涨幅
        BigDecimal todayChangePercentage = client.fetchCurrencyTodayChangePercentage(market);
        specifyCurrencyMonitoring.setChangePercentageToday(todayChangePercentage.setScale(2, RoundingMode.HALF_UP) + "%");
        //24小时涨幅
        specifyCurrencyMonitoring.setChangePercentage24(tickerNow.getChangePercentage());
        specifyCurrencyMonitoring.setMonitoringBaseResultList(baseResultList);
        return specifyCurrencyMonitoring;
    }

    @Override
    protected Boolean specialProcess(Map<String, String> monitoringParam, SpecifyCurrencyMonitoringDTO dataResult, GateApiExtension api) {
        List<MonitoringBaseResultDTO> dataResultList = dataResult.getMonitoringBaseResultList();
        if(CollectionUtils.isEmpty(dataResultList)){
            return  Boolean.FALSE;
        }
        return  Boolean.TRUE;
    }

    /**
     * 交易流程
     *
     * @param dataResult
     * @param api
     * @return
     */
    @Override
    protected EatSpotOrderMarketResultDTO transactionProcess(SpecifyCurrencyMonitoringDTO dataResult, GateApiExtension api) {
        return null;
    }

    /**
     * 构造消息消息
     *
     * @param dataResult
     * @param transaction
     * @return
     */
    @Override
    protected String buildWxMessage(SpecifyCurrencyMonitoringDTO dataResult, EatSpotOrderMarketResultDTO transaction) {
        StringBuffer message = new StringBuffer();
        List<MonitoringBaseResultDTO> dataResultList = dataResult.getMonitoringBaseResultList();
        if(CollectionUtils.isEmpty(dataResultList)){
            return  null;
        }
        //交易对
        String currency = dataResult.getCurrency();
        //当前价格
        String currentPrice = dataResult.getCurrentPrice().toString();
        //用第一条数据判断当前走势
        MonitoringBaseResultDTO first = dataResultList.get(0);
        //设置标题
        String warnMsg = CurrencyUtils.convertWarnMsg(first.getFall());
        String color = CurrencyUtils.convertColorValue(first.getFall());
        message.append("<font size=6 color=").append(color).append(">特别提示[").append(currency).append("]可能").append(warnMsg).append("</font> \n ");
        //振幅金额
        SpecifyCurrencyWarningMonitor.buildMessageTitle(dataResult, message, currentPrice);
        SpecifyCurrencyWarningMonitor.buildMessageContent(message, dataResultList);
        message.append("<b><font color=red>市场变化剧烈, 请密切关注行情走势。</font></b>\n");
        message.append("数据来源【gate.io】");
        return message.toString();
    }
}
