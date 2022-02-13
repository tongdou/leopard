package io.github.leopard.core.strategy.impl;

import io.github.leopard.common.exception.ServiceException;
import io.github.leopard.common.utils.BigDecimalUtil;
import io.github.leopard.common.utils.CommonUtils;
import io.github.leopard.exchange.extension.GateApiExtension;
import io.github.leopard.exchange.model.dto.Result;
import io.github.leopard.exchange.model.dto.request.PrevCandlestickRequestDTO;
import io.github.leopard.exchange.model.dto.request.SpotPriceTriggeredOrderRequestDTO;
import io.github.leopard.exchange.model.dto.result.CandlestickResultDTO;
import io.github.leopard.exchange.model.dto.result.SpotPriceTriggeredOrderResultDTO;
import io.github.leopard.exchange.model.dto.result.TickResultDTO;
import io.github.leopard.exchange.model.enums.CandlesticksIntervalEnum;
import io.github.leopard.exchange.model.enums.RuleEnum;
import io.github.leopard.exchange.model.enums.SideEnum;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 动态追踪辅助类
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Slf4j
public class BandTrackingStrategySupport {

    private GateApiExtension api;

    public BandTrackingStrategySupport(GateApiExtension api) {
        this.api = api;
    }

    private String prevTriggerOrderId; //上次触发单订单号

    public void syncExecute(String market, BigDecimal buyerPrice, BigDecimal tokenAmt, BigDecimal percent,
            CandlesticksIntervalEnum intervalEnum) {

        CandlestickResultDTO prevCandlestick = api.candlestickRecentMust(market, intervalEnum);
        String prevDateTimeString = prevCandlestick.getDateTimeString();
        log.info("[{}][{}]开始波段追踪，第一次追踪所在时间周期为[{}]", market, intervalEnum.getValue(), prevDateTimeString);

        //动态最高价
        BigDecimal prevHighest = prevCandlestick.getHighest();

        //动态触发卖出价格
        BigDecimal triggerSellPrice = this.createFirstTriggerOrder(market, prevDateTimeString, tokenAmt, buyerPrice,
                prevHighest, percent, intervalEnum);

        log.info("[{}][{}][{}]完成初始触发价格设置，开始循环波段追踪。", market, intervalEnum.getValue(), prevDateTimeString);

        while (true) {
            CandlestickResultDTO curCandlestick = api.candlestickRecentMust(market, intervalEnum);

            TickResultDTO tickResultDTO = api.getTickerMust(market);
            BigDecimal lastPrice = tickResultDTO.getLast(); //最新价
            BigDecimal curHighest = curCandlestick.getHighest(); //当前最高价

            String curDateTimeString = curCandlestick.getDateTimeString();

            if (StringUtils.equals(prevDateTimeString, curDateTimeString)) {

                //当最新成交价小于卖出触发价格后，交易所就会帮我们挂一个委托卖出订单，但是这个委托单可能没有成交
                // 目前的原因有几方面：
                // 1) 价格剧烈波动：从触发单到委托单挂上的时间段内（比如1秒）价格已经跌破了 触发卖出价，所以就一直在高位挂单了。
                // 这种现象好像叫穿仓
                if (lastPrice.compareTo(triggerSellPrice) <= 0) {
                    break;
                }

                //破新高
                if (curHighest.compareTo(prevHighest) > 0) {
                    triggerSellPrice = this.higherHighCreateTriggerOrder(market, prevDateTimeString, tokenAmt,
                            buyerPrice, curHighest, triggerSellPrice, percent, intervalEnum);
                    prevHighest = curHighest;
                }

            } else {

                //因为我们获取有间隔，所以可能会在间隔中缺少数据，这里取按照区间查询拿到最后一个即可
                PrevCandlestickRequestDTO prevCandlestickRequestDTO = new PrevCandlestickRequestDTO();
                prevCandlestickRequestDTO.setMarket(market);
                prevCandlestickRequestDTO.setIntervalEnum(intervalEnum);
                prevCandlestickRequestDTO.setCurDateTime(curCandlestick.getDateTime());
                prevCandlestick = api.prevCandlestickMust(prevCandlestickRequestDTO);

                //切换可能会出现一个新的最高价 不要错过
                if (prevCandlestick.getHighest().compareTo(prevHighest) > 0) {
                    prevHighest = prevCandlestick.getHighest();
                }

                prevDateTimeString = curDateTimeString;
            }
            CommonUtils.sleepSeconds(1);
        }
    }


    /**
     * 破新高时更新卖出触发价
     */
    private BigDecimal higherHighCreateTriggerOrder(String market, String prevDateTimeString, BigDecimal tokenAmt,
            BigDecimal buyerPrice, BigDecimal curHighest, BigDecimal prevTriggerSellPrice, BigDecimal percent,
            CandlesticksIntervalEnum intervalEnum) {

        BigDecimal downPercent = BigDecimal.ONE.subtract(percent); //回撤百分比

        //成本止损价格
        BigDecimal buyerDown = buyerPrice.multiply(downPercent);
        //最高点回撤价格
        BigDecimal highestDown = curHighest.multiply(downPercent);

        BigDecimal triggerSellPrice = curHighest.compareTo(buyerPrice) > 0 ? highestDown : buyerDown;

        SpotPriceTriggeredOrderResultDTO orderResponse = this.createTriggerOrder(market, triggerSellPrice, triggerSellPrice, tokenAmt);

        //是否已盈利
        boolean profit = prevTriggerSellPrice.compareTo(buyerPrice) > 0;

        //最高价盈利比例
        final BigDecimal curHighestPercent = changePercent(buyerPrice, curHighest);
        //触发单盈利比例
        final BigDecimal triggerSellPricePercent = changePercent(buyerPrice, triggerSellPrice);

        String message;
        if (profit) {
            message = String.format("[%s][%s][%s]盈利态破新高[%s][%s]，已创建触发单[%s][%s]", market, intervalEnum.getValue(),
                    prevDateTimeString, curHighestPercent, triggerSellPricePercent, orderResponse.getOrderId(), triggerSellPrice);
        } else {
            message = String.format("[%s][%s][%s]亏损态破新高[%s][%s]，已创建触发单[%s][%s]", market, intervalEnum.getValue(),
                    prevDateTimeString, curHighestPercent, triggerSellPricePercent, orderResponse.getOrderId(), triggerSellPrice);
        }

        log.info(message);

        return triggerSellPrice;
    }


    /**
     * 计算涨幅
     */
    private BigDecimal changePercent(BigDecimal prev, BigDecimal cur) {
        BigDecimal percent = (cur.divide(prev, 8, RoundingMode.HALF_UP).subtract(new BigDecimal("1")))
                .multiply(new BigDecimal("100"));
        return BigDecimalUtil.roundingHalfUp(percent);
    }

    /**
     * 首次创建触发单
     */
    private BigDecimal createFirstTriggerOrder(String market, String prevDateTimeString, BigDecimal tokenAmt,
            BigDecimal buyerPrice, BigDecimal curHighest, BigDecimal percent, CandlesticksIntervalEnum intervalEnum) {

        BigDecimal downPercent = BigDecimal.ONE.subtract(percent); //回撤百分比

        //成本止损价格
        BigDecimal buyerDown = buyerPrice.multiply(downPercent);
        //最高点回撤价格
        BigDecimal highestDown = curHighest.multiply(downPercent);

        BigDecimal triggerSellPrice = highestDown.compareTo(buyerPrice) > 0 ? highestDown : buyerDown;

        SpotPriceTriggeredOrderResultDTO orderResponse = this.createTriggerOrder(market, triggerSellPrice, triggerSellPrice, tokenAmt);
        String message = String.format("[%s][%s][%s]刚买入，已创建触发单[%s][%s]", market, intervalEnum.getValue(),
                prevDateTimeString, orderResponse.getOrderId(), triggerSellPrice);
        log.info(message);

        return triggerSellPrice;
    }


    private SpotPriceTriggeredOrderResultDTO createTriggerOrder(String market, BigDecimal triggerSellPrice, BigDecimal sellPrice,
            BigDecimal tokenAmt) throws ServiceException {

        log.info("[{}]准备创建新的自动订单，triggerSellPrice={}，sellPrice={}", market, triggerSellPrice, sellPrice);
        SpotPriceTriggeredOrderRequestDTO triggeredOrderRequestDTO = new SpotPriceTriggeredOrderRequestDTO();
        triggeredOrderRequestDTO.setMarket(market);
        triggeredOrderRequestDTO.setPrice(sellPrice);
        triggeredOrderRequestDTO.setTriggerPrice(triggerSellPrice);
        triggeredOrderRequestDTO.setTokenAmt(tokenAmt);
        triggeredOrderRequestDTO.setTriggerExpiration(3600 * 24 * 7);
        triggeredOrderRequestDTO.setTriggerRule(RuleEnum.LESS_THAN_OR_EQUAL_TO);
        triggeredOrderRequestDTO.setSideEnum(SideEnum.SELL);
        SpotPriceTriggeredOrderResultDTO orderResultDTO = api.createSpotPriceTriggeredOrderMust(triggeredOrderRequestDTO);
        log.info("[{}]自动订单创建成功，orderId={}", market, orderResultDTO.getOrderId());

        //取消之前的自动订单
        if (StringUtils.isBlank(prevTriggerOrderId)) {
            log.info("[{}]第一次执行，不取消自动订单。", market);
        } else {
            api.cancelSpotPriceTriggeredOrderMust(prevTriggerOrderId);
            log.info("[{}]自动订单已取消，prevTriggerOrderId={}", market, prevTriggerOrderId);
        }

        this.prevTriggerOrderId = orderResultDTO.getOrderId(); //下次取消使用

        return orderResultDTO;
    }


}
