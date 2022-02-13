package io.github.leopard.exchange.extension;

import io.github.leopard.common.utils.CommonUtils;
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
     * 以一定的周期开始监控一个市场
     */
    public static void syncMonitor(String market, CandlesticksIntervalEnum interval, CandlestickChangeListener changeListener) {

        GateApiExtension api = GateApiExtension.create();

        CandlestickResultDTO prevCandlestick = api.candlestickRecentMust(market, interval);

        String prevDateTimeString = prevCandlestick.getDateTimeString();
        log.info("[{}][{}]开始监控K线，第一次监控所在时间周期为[{}]", market, interval.getValue(), prevDateTimeString);

        while (true) {
            CandlestickResultDTO curCandlestick = api.candlestickRecentMust(market, interval);
            String curDateTimeString = curCandlestick.getDateTimeString();

            if (StringUtils.equals(prevDateTimeString, curDateTimeString)) {

            } else {
                log.info("[{}][{}][{}]K线周期切换为[{}]", market, interval.getValue(), prevDateTimeString, curDateTimeString);

                //因为我们获取有间隔，所以可能会在间隔中缺少数据，这里取按照区间查询拿到最后一个即可
                PrevCandlestickRequestDTO prevCandlestickRequestDTO = new PrevCandlestickRequestDTO();
                prevCandlestickRequestDTO.setMarket(market);
                prevCandlestickRequestDTO.setIntervalEnum(interval);
                prevCandlestickRequestDTO.setCurDateTime(curCandlestick.getDateTime());
                prevCandlestick = api.prevCandlestickMust(prevCandlestickRequestDTO);

                prevDateTimeString = curDateTimeString;
                CandlestickMonitorStatus monitorStatus = changeListener.onchange(prevCandlestick, curCandlestick, interval);
                if (monitorStatus.equals(CandlestickMonitorStatus.STOP)) {
                    break;
                }
            }
            CommonUtils.sleepSeconds(1);
        }
    }


    public interface CandlestickChangeListener {

        CandlestickMonitorStatus onchange(CandlestickResultDTO prev, CandlestickResultDTO cur, CandlesticksIntervalEnum interval);
    }

    public enum CandlestickMonitorStatus {
        RUNNING, //继续运行
        STOP;  //终止监控
    }
}
