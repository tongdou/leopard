package io.github.leopard.exchange.extension;

import io.github.leopard.common.utils.CommonUtils;
import io.github.leopard.exchange.model.dto.request.CandlestickRequestDTO;
import io.github.leopard.exchange.model.dto.request.PrevCandlestickRequestDTO;
import io.github.leopard.exchange.model.dto.result.CandlestickResultDTO;
import io.github.leopard.exchange.model.enums.CandlesticksIntervalEnum;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Slf4j
public class MarketMonitor {


    /**
     * 以一定的周期开始监控一个市场 这个方法不会抛出异常
     */
    public static void syncMonitor(String market, CandlesticksIntervalEnum interval, TickerChangeListener changeListener) {

        GateApiExtension api = GateApiExtension.create();

        CandlestickResultDTO prevTicker = api.candlestickRecentMust(market, interval);

        String prevDateTimeString = prevTicker.getDateTimeString();
        log.info("[{}][{}]开始监控K线，第一次监控所在时间周期为[{}]", market, interval.getValue(), prevDateTimeString);

        while (true) {
            CandlestickResultDTO curTicker = api.candlestickRecentMust(market, interval);
            String curDateTimeString = curTicker.getDateTimeString();

            if (StringUtils.equals(prevDateTimeString, curDateTimeString)) {

            } else {
                log.info("[{}][{}][{}]K线周期切换为[{}]", market, interval.getValue(), prevDateTimeString, curDateTimeString);

                //因为我们获取有间隔，所以可能会在间隔中缺少数据，这里取按照区间查询拿到最后一个即可
                PrevCandlestickRequestDTO prevCandlestickRequestDTO = new PrevCandlestickRequestDTO();
                prevCandlestickRequestDTO.setMarket(market);
                prevCandlestickRequestDTO.setIntervalEnum(CandlesticksIntervalEnum.M_5);
                prevCandlestickRequestDTO.setCurDateTime(LocalDateTime.now());
                prevTicker = api.prevCandlestickMust(prevCandlestickRequestDTO);

                prevDateTimeString = curDateTimeString;
                try {
                    changeListener.onchange(prevTicker, curTicker, interval);
                } catch (InterruptedException e) {
                    //其它异常不要影响监控的运行
                    break;
                }
            }
            CommonUtils.sleepSeconds(1);
        }
    }


    public interface TickerChangeListener {

        void onchange(CandlestickResultDTO prev, CandlestickResultDTO cur, CandlesticksIntervalEnum interval)
                throws InterruptedException;
    }

}
