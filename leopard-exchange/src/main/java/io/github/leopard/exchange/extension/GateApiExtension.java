package io.github.leopard.exchange.extension;

import io.github.leopard.common.utils.CommonUtils;
import io.github.leopard.common.utils.DateFormatEnum;
import io.github.leopard.exchange.client.GateApi;
import io.github.leopard.exchange.exception.ExchangeApiException;
import io.github.leopard.exchange.model.dto.Result;
import io.github.leopard.exchange.model.dto.UserSecretDTO;
import io.github.leopard.exchange.model.dto.request.CandlestickRequestDTO;
import io.github.leopard.exchange.model.dto.request.CreateSpotOrderRequestDTO;
import io.github.leopard.exchange.model.dto.request.PrevCandlestickRequestDTO;
import io.github.leopard.exchange.model.dto.request.SpotPriceTriggeredOrderRequestDTO;
import io.github.leopard.exchange.model.dto.request.TickRequestDTO;
import io.github.leopard.exchange.model.dto.result.CandlestickResultDTO;
import io.github.leopard.exchange.model.dto.result.CreateSpotOrderResultDTO;
import io.github.leopard.exchange.model.dto.result.SpotPriceTriggeredOrderResultDTO;
import io.github.leopard.exchange.model.dto.result.TickResultDTO;
import io.github.leopard.exchange.model.enums.CandlesticksIntervalEnum;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * GATE交易
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
public class GateApiExtension extends GateApi {

    private GateApiExtension(UserSecretDTO userSecretDTO) {
        super(userSecretDTO);
    }

    public static GateApiExtension auth(UserSecretDTO userSecretDTO) {
        return new GateApiExtension(userSecretDTO);
    }

    public static GateApiExtension create() {
        return new GateApiExtension(new UserSecretDTO(StringUtils.EMPTY, StringUtils.EMPTY));
    }

    /**
     * 创建现货触发订单
     */
    public Result<SpotPriceTriggeredOrderResultDTO> createSpotPriceTriggeredOrder(SpotPriceTriggeredOrderRequestDTO request) {
        try {
            return Result.success(super.createSpotPriceTriggeredOrderCore(request));
        } catch (ExchangeApiException e) {
            return Result.fail(e.getResultCode());
        }
    }

    /**
     * 挂现货单
     */
    public Result<CreateSpotOrderResultDTO> createSpotOrder(CreateSpotOrderRequestDTO request) {
        try {
            return Result.success(super.createSpotOrderCore(request));
        } catch (ExchangeApiException e) {
            return Result.fail(e.getResultCode());
        }
    }

    /**
     * 获取Ticker信息
     * <b>该方法一定会返回结果，未返回前会一直阻塞，该方法不会抛出异常。<b>
     */
    public TickResultDTO getTickerMust(String market) {
        TickRequestDTO requestDTO = new TickRequestDTO();
        requestDTO.setMarket(market);
        while (true) {
            try {
                return super.getTickerCore(requestDTO);
            } catch (ExchangeApiException e) {
                CommonUtils.sleepSeconds(1);
            }
        }
    }

    /**
     * 获取蜡烛图
     * <b>该方法一定会返回结果，未返回前会一直阻塞，该方法不会抛出异常。<b>
     */
    public List<CandlestickResultDTO> listCandlestickMust(CandlestickRequestDTO request) {
        while (true) {
            try {
                return super.listCandlesticksCore(request);
            } catch (ExchangeApiException e) {
                CommonUtils.sleepSeconds(1);
            }
        }
    }

    /**
     * 获取上一个周期的蜡烛图
     * <b>该方法一定会返回结果，未返回前会一直阻塞，该方法不会抛出异常。<b>
     */
    public CandlestickResultDTO prevCandlestickMust(PrevCandlestickRequestDTO request) {
        LocalDateTime curDateTime = request.getCurDateTime();
        CandlesticksIntervalEnum intervalEnum = request.getIntervalEnum();
        String market = request.getMarket();
        CandlestickRequestDTO candlestickRequestDTO = new CandlestickRequestDTO();
        candlestickRequestDTO.setIntervalEnum(intervalEnum);
        candlestickRequestDTO.setMarket(market);
        long to;
        String name = intervalEnum.name();
        String[] periods = name.split("_");
        String period = periods[0];
        String unit = periods[1];
        //当前周期
        if (StringUtils.equals(period, "M")) { //分钟
            LocalDateTime prevMin = curDateTime.minusMinutes(Long.parseLong(unit));
            to = DateFormatEnum.DATETIME_DEFAULT.parseToUtilDate(prevMin).getTime() / 1000;
        } else if (StringUtils.equals(period, "S")) {   //秒
            LocalDateTime prevSeconds = curDateTime.minusSeconds(Long.parseLong(unit));
            to = DateFormatEnum.DATETIME_DEFAULT.parseToUtilDate(prevSeconds).getTime() / 1000;
        } else if (StringUtils.equals(period, "H")) { //小时
            LocalDateTime prevHour = curDateTime.minusHours(Long.parseLong(unit));
            to = DateFormatEnum.DATETIME_DEFAULT.parseToUtilDate(prevHour).getTime() / 1000;
        } else if (StringUtils.equals(period, "D")) { //天
            LocalDateTime prevDay = curDateTime.minusDays(Long.parseLong(unit));
            to = DateFormatEnum.DATETIME_DEFAULT.parseToUtilDate(prevDay).getTime() / 1000;
        }
        while (true) {
            try {
                return super.listCandlesticksCore(candlestickRequestDTO).get(0);
            } catch (Throwable e) {
                CommonUtils.sleepSeconds(1);
            }
        }
    }

}
