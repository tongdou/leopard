package io.github.leopard.exchange.extension;

import com.alibaba.fastjson.JSON;
import io.github.leopard.common.utils.CommonUtils;
import io.github.leopard.exchange.client.GateApi;
import io.github.leopard.exchange.exception.ExchangeApiException;
import io.github.leopard.exchange.exception.ExchangeResultCodeEnum;
import io.github.leopard.exchange.model.dto.ExchangeUserSecretDTO;
import io.github.leopard.exchange.model.dto.Result;
import io.github.leopard.exchange.model.dto.request.CancelSpotPriceTriggeredOrderRequestDTO;
import io.github.leopard.exchange.model.dto.request.CandlestickRequestDTO;
import io.github.leopard.exchange.model.dto.request.CreateSpotOrderRequestDTO;
import io.github.leopard.exchange.model.dto.request.CurrencyPairRequestDTO;
import io.github.leopard.exchange.model.dto.request.EatSpotOrderMarketRequestDTO;
import io.github.leopard.exchange.model.dto.request.ListOrderBookRequestDTO;
import io.github.leopard.exchange.model.dto.request.PrevCandlestickRequestDTO;
import io.github.leopard.exchange.model.dto.request.SpotAccountRequestDTO;
import io.github.leopard.exchange.model.dto.request.SpotOderQueryRequestDTO;
import io.github.leopard.exchange.model.dto.request.SpotPriceTriggeredOrderRequestDTO;
import io.github.leopard.exchange.model.dto.request.TickRequestDTO;
import io.github.leopard.exchange.model.dto.result.CancelSpotPriceTriggeredOrderResultDTO;
import io.github.leopard.exchange.model.dto.result.CandlestickResultDTO;
import io.github.leopard.exchange.model.dto.result.CreateSpotOrderResultDTO;
import io.github.leopard.exchange.model.dto.result.CurrencyPairResultDTO;
import io.github.leopard.exchange.model.dto.result.EatSpotOrderMarketResultDTO;
import io.github.leopard.exchange.model.dto.result.ListOrderBookResultDTO;
import io.github.leopard.exchange.model.dto.result.ListOrderBookResultDTO.AskDTO;
import io.github.leopard.exchange.model.dto.result.SpotAccountResultDTO;
import io.github.leopard.exchange.model.dto.result.SpotPriceTriggeredOrderResultDTO;
import io.github.leopard.exchange.model.dto.result.TickResultDTO;
import io.github.leopard.exchange.model.enums.CandlesticksIntervalEnum;
import io.github.leopard.exchange.model.enums.OrderStatusEnum;
import io.github.leopard.exchange.model.enums.SideEnum;
import io.github.leopard.exchange.model.enums.TimeInForceEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * GATE交易
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
public class GateApiExtension extends GateApi {

    private GateApiExtension(ExchangeUserSecretDTO userSecretDTO) {
        super(userSecretDTO);
    }

    public static GateApiExtension auth(ExchangeUserSecretDTO userSecretDTO) {
        return new GateApiExtension(userSecretDTO);
    }

    public static GateApiExtension create() {
        return new GateApiExtension(new ExchangeUserSecretDTO(StringUtils.EMPTY, StringUtils.EMPTY));
    }


    /**
     * 查询所有币种信息
     * <b>该方法一定会返回结果，未返回前会一直阻塞，该方法不会抛出异常。<b>
     */
    public CurrencyPairResultDTO currencyPairsMust(String pair) {
        CurrencyPairRequestDTO requestDTO = new CurrencyPairRequestDTO();
        requestDTO.setPair(pair);
        while (true) {
            try {
                return super.currencyPairsCore(requestDTO);
            } catch (ExchangeApiException e) {
                CommonUtils.sleepSeconds(1);
            }
        }
    }

    /**
     * 查询所有币种信息
     * <b>该方法一定会返回结果，未返回前会一直阻塞，该方法不会抛出异常。<b>
     */
    public List<CurrencyPairResultDTO> listCurrencyPairsMust() {
        while (true) {
            try {
                return super.listCurrencyPairsCore();
            } catch (ExchangeApiException e) {
                CommonUtils.sleepSeconds(1);
            }
        }
    }


    /**
     * 查询现货账户
     */
    public SpotAccountResultDTO spotAccountMust(String currency) {
        SpotAccountRequestDTO requestDTO = new SpotAccountRequestDTO();
        requestDTO.setCurrency(currency);
        while (true) {
            try {
                return super.spotAccountCore(requestDTO);
            } catch (ExchangeApiException e) {
                CommonUtils.sleepSeconds(1);
            }
        }
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
     * 创建现货触发订单
     * <b>该方法一定会返回结果，未返回前会一直阻塞，该方法不会抛出异常。<b>
     */
    public SpotPriceTriggeredOrderResultDTO createSpotPriceTriggeredOrderMust(SpotPriceTriggeredOrderRequestDTO request) {
        while (true) {
            try {
                return super.createSpotPriceTriggeredOrderCore(request);
            } catch (ExchangeApiException e) {
                CommonUtils.sleepSeconds(1);
            }
        }
    }


    /**
     * 取消现货触发订单
     * <b>该方法一定会返回结果，未返回前会一直阻塞，该方法不会抛出异常。<b>
     */
    public CancelSpotPriceTriggeredOrderResultDTO cancelSpotPriceTriggeredOrderMust(String orderId) {
        CancelSpotPriceTriggeredOrderRequestDTO requestDTO = new CancelSpotPriceTriggeredOrderRequestDTO();
        requestDTO.setOrderId(orderId);
        while (true) {
            try {
                return super.cancelSpotPriceTriggeredOrderCore(requestDTO);
            } catch (ExchangeApiException e) {
                CommonUtils.sleepSeconds(1);
            }
        }
    }


    /**
     * 市价吃单（全部成交）
     *
     * <b>到达最大重试次数前都会以市价重试<b>
     */
    public EatSpotOrderMarketResultDTO eatSpotOrderMarketMustOrNull(EatSpotOrderMarketRequestDTO request) {

        int cur = 1;
        while (cur <= request.getRetryMax()) {
            //获取最新成交价
            BigDecimal lastPrice = this.getTickerMust(request.getMarket()).getLast();

            CreateSpotOrderRequestDTO orderRequestDTO = new CreateSpotOrderRequestDTO();
            orderRequestDTO.setPrice(lastPrice.add(new BigDecimal("0.00001")));   //加一点钱，排到前面去
            orderRequestDTO.setTokenAmt(request.getUsdtAmt().divide(orderRequestDTO.getPrice(), 5, BigDecimal.ROUND_FLOOR));
            orderRequestDTO.setSideEnum(SideEnum.BUY);
            orderRequestDTO.setTimeInForceEnum(TimeInForceEnum.IOC);
            orderRequestDTO.setMarket(request.getMarket());
            orderRequestDTO.setText(request.getText());

            Result<CreateSpotOrderResultDTO> spotOrderResponse = this.createSpotOrder(orderRequestDTO);


            if (spotOrderResponse.isSuccess() &&
                    spotOrderResponse.getData().getOrderStatusEnum().equals(OrderStatusEnum.CLOSED)) {


                EatSpotOrderMarketResultDTO resultDTO = new EatSpotOrderMarketResultDTO();
                resultDTO.setTokenAmt(orderRequestDTO.getTokenAmt());
                resultDTO.setPrice(orderRequestDTO.getPrice());
                resultDTO.setMarket(request.getMarket());
                resultDTO.setOrderId(spotOrderResponse.getData().getOrderId());
                log.info("最新成交价吃单结束，resultDTO={}", JSON.toJSONString(resultDTO));
                return resultDTO;
            } else {
                //TODO 超时查证 否则会循环下单 !!!
                log.error("最新成交价吃单异常，message={}", spotOrderResponse.getMsg());
            }

            //获取卖一价
            ListOrderBookRequestDTO requestDTO = new ListOrderBookRequestDTO();
            requestDTO.setMarket(request.getMarket());
            ListOrderBookResultDTO bookResultDTO = listOrderBookMust(requestDTO);
            AskDTO askDTO = bookResultDTO.getAsks().get(0);

            orderRequestDTO.setPrice(askDTO.getPrice().add(new BigDecimal("0.00001")));
            orderRequestDTO.setTokenAmt(request.getUsdtAmt().divide(orderRequestDTO.getPrice(), 5, BigDecimal.ROUND_FLOOR));

            spotOrderResponse = this.createSpotOrder(orderRequestDTO);

            if (spotOrderResponse.isSuccess() &&
                    spotOrderResponse.getData().getOrderStatusEnum().equals(OrderStatusEnum.CLOSED)) {

                EatSpotOrderMarketResultDTO resultDTO = new EatSpotOrderMarketResultDTO();
                resultDTO.setTokenAmt(orderRequestDTO.getTokenAmt());
                resultDTO.setPrice(orderRequestDTO.getPrice());
                resultDTO.setMarket(request.getMarket());
                resultDTO.setOrderId(spotOrderResponse.getData().getOrderId());
                log.info("卖一价吃单结束，resultDTO={}", JSON.toJSONString(resultDTO));
                return resultDTO;
            } else {
                //TODO 超时查证 否则会循环下单 !!!
                log.error("卖一价吃单异常，message={}", spotOrderResponse.getMsg());
            }
            cur++;
        }
        return null;
    }


    /**
     * 现货下单
     */
    public Result<CreateSpotOrderResultDTO> createSpotOrder(CreateSpotOrderRequestDTO request) {
        try {
            return Result.success(super.createSpotOrderCore(request));
        } catch (ExchangeApiException e) {
            return Result.fail(e.getResultCode());
        }
    }

    /**
     * 查询单个订单详情，不存在时返回null
     */
    public CreateSpotOrderResultDTO spotOderQueryMustOrNull(SpotOderQueryRequestDTO sourceRequest) {
        while (true) {
            try {
                return super.spotOderQueryCore(sourceRequest);
            } catch (ExchangeApiException e) {
                if (e.getResultCode().equals(ExchangeResultCodeEnum.ORDER_NOT_FOUND)) {
                    return null;
                }
                CommonUtils.sleepSeconds(1);
            }
        }
    }

    /**
     * 查询市场深度信息
     * <b>该方法一定会返回结果，未返回前会一直阻塞，该方法不会抛出异常。<b>
     */
    public ListOrderBookResultDTO listOrderBookMust(ListOrderBookRequestDTO requestDTO) {
        while (true) {
            try {
                return super.listOrderBookCore(requestDTO);
            } catch (ExchangeApiException e) {
                CommonUtils.sleepSeconds(1);
            }
        }
    }

    /**
     * 获取单个Ticker信息
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
     * 获取所有Ticker信息
     * <b>该方法一定会返回结果，未返回前会一直阻塞，该方法不会抛出异常。<b>
     */
    public List<TickResultDTO> getTickerMust() {
        while (true) {
            try {
                return super.getTickersCore();
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
     * 获取最新的蜡烛图，一根K线
     * <b>该方法一定会返回结果，未返回前会一直阻塞，该方法不会抛出异常。<b>
     */
    public CandlestickResultDTO candlestickRecentMust(String market, CandlesticksIntervalEnum intervalEnum) {
        CandlestickRequestDTO requestDTO = new CandlestickRequestDTO();
        requestDTO.setMarket(market);
        requestDTO.setIntervalEnum(intervalEnum);
        requestDTO.setLimit(1);
        return listCandlestickMust(requestDTO).get(0);
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
        String name = intervalEnum.name();
        String[] periods = name.split("_");
        String period = periods[0];
        String unit = periods[1];
        //当前周期
        if (StringUtils.equals(period, "M")) { //分钟
            LocalDateTime prevMin = curDateTime.minusMinutes(Long.parseLong(unit));
            candlestickRequestDTO.setFrom(prevMin);
            candlestickRequestDTO.setTo(prevMin);
        } else if (StringUtils.equals(period, "S")) {   //秒
            LocalDateTime prevSeconds = curDateTime.minusSeconds(Long.parseLong(unit));
            candlestickRequestDTO.setFrom(prevSeconds);
            candlestickRequestDTO.setTo(prevSeconds);
        } else if (StringUtils.equals(period, "H")) { //小时
            LocalDateTime prevHour = curDateTime.minusHours(Long.parseLong(unit));
            candlestickRequestDTO.setFrom(prevHour);
            candlestickRequestDTO.setTo(prevHour);
        } else if (StringUtils.equals(period, "D")) { //天
            LocalDateTime prevDay = curDateTime.minusDays(Long.parseLong(unit));
            candlestickRequestDTO.setFrom(prevDay);
            candlestickRequestDTO.setTo(prevDay);
        }
        while (true) {
            try {
                return super.listCandlesticksCore(candlestickRequestDTO).get(0);
            } catch (ExchangeApiException e) {
                CommonUtils.sleepSeconds(1);
            }
        }
    }

}
