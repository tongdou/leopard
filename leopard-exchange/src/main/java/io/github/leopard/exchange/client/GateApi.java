package io.github.leopard.exchange.client;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import io.gate.gateapi.ApiClient;
import io.gate.gateapi.ApiException;
import io.gate.gateapi.Configuration;
import io.gate.gateapi.GateApiException;
import io.gate.gateapi.api.SpotApi;
import io.gate.gateapi.api.SpotApi.APIlistCandlesticksRequest;
import io.gate.gateapi.models.CurrencyPair;
import io.gate.gateapi.models.Order;
import io.gate.gateapi.models.OrderBook;
import io.gate.gateapi.models.SpotAccount;
import io.gate.gateapi.models.SpotPricePutOrder;
import io.gate.gateapi.models.SpotPricePutOrder.AccountEnum;
import io.gate.gateapi.models.SpotPriceTrigger;
import io.gate.gateapi.models.SpotPriceTrigger.RuleEnum;
import io.gate.gateapi.models.SpotPriceTriggeredOrder;
import io.gate.gateapi.models.Ticker;
import io.gate.gateapi.models.TriggerOrderResponse;
import io.github.leopard.common.exception.ServiceException;
import io.github.leopard.common.utils.DateFormatEnum;
import io.github.leopard.common.utils.NullUtils;
import io.github.leopard.common.utils.bean.BeanUtils;
import io.github.leopard.exchange.exception.ExchangeApiException;
import io.github.leopard.exchange.exception.ExchangeResultCodeEnum;
import io.github.leopard.exchange.model.dto.ExchangeUserSecretDTO;
import io.github.leopard.exchange.model.dto.request.CancelSpotPriceTriggeredOrderRequestDTO;
import io.github.leopard.exchange.model.dto.request.CandlestickRequestDTO;
import io.github.leopard.exchange.model.dto.request.CreateSpotOrderRequestDTO;
import io.github.leopard.exchange.model.dto.request.CurrencyPairRequestDTO;
import io.github.leopard.exchange.model.dto.request.ListOrderBookRequestDTO;
import io.github.leopard.exchange.model.dto.request.SpotAccountRequestDTO;
import io.github.leopard.exchange.model.dto.request.SpotOderQueryRequestDTO;
import io.github.leopard.exchange.model.dto.request.SpotPriceTriggeredOrderRequestDTO;
import io.github.leopard.exchange.model.dto.request.TickRequestDTO;
import io.github.leopard.exchange.model.dto.result.CancelSpotPriceTriggeredOrderResultDTO;
import io.github.leopard.exchange.model.dto.result.CandlestickResultDTO;
import io.github.leopard.exchange.model.dto.result.CreateSpotOrderResultDTO;
import io.github.leopard.exchange.model.dto.result.CurrencyPairResultDTO;
import io.github.leopard.exchange.model.dto.result.ListOrderBookResultDTO;
import io.github.leopard.exchange.model.dto.result.ListOrderBookResultDTO.AskDTO;
import io.github.leopard.exchange.model.dto.result.ListOrderBookResultDTO.BidDTO;
import io.github.leopard.exchange.model.dto.result.SpotAccountResultDTO;
import io.github.leopard.exchange.model.dto.result.SpotPriceTriggeredOrderResultDTO;
import io.github.leopard.exchange.model.dto.result.TickResultDTO;
import io.github.leopard.exchange.model.enums.OrderStatusEnum;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * GATE交易
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
public class GateApi implements IExchangeApi{


    protected final Logger log = LoggerFactory.getLogger(getClass());

    private ExchangeUserSecretDTO userSecretDTO;

    protected GateApi(ExchangeUserSecretDTO userSecretDTO) {
        this.userSecretDTO = userSecretDTO;
    }


    /**
     * 查询单个币种信息
     */
    protected CurrencyPairResultDTO currencyPairsCore(CurrencyPairRequestDTO requestDTO) throws ExchangeApiException {
        final SpotApi apiInstance = createSpotApi();
        try {
            CurrencyPair response = apiInstance.getCurrencyPair(requestDTO.getPair());
            CurrencyPairResultDTO resultDTO = new CurrencyPairResultDTO();
            BeanUtils.copyProperties(response, resultDTO);
            return resultDTO;
        } catch (ApiException e) {
            log.warn("[查询单个币种信息]异常，code={}，message={}", e.getCode(), e.getMessage());
            Throwable sourceCause = e.getCause();
            if (sourceCause instanceof IOException) {
                throw new ExchangeApiException(ExchangeResultCodeEnum.TIMEOUT);
            } else {
                throw new ExchangeApiException(ExchangeResultCodeEnum.FAIL, e);
            }
        }
    }


    /**
     * 查询所有币种信息
     */
    protected List<CurrencyPairResultDTO> listCurrencyPairsCore() throws ExchangeApiException {
        final SpotApi apiInstance = createSpotApi();
        try {
            List<CurrencyPairResultDTO> result = new ArrayList<>();
            List<CurrencyPair> response = apiInstance.listCurrencyPairs();
            for (CurrencyPair pair : response) {
                CurrencyPairResultDTO resultDTO = new CurrencyPairResultDTO();
                BeanUtils.copyProperties(pair, resultDTO);
                result.add(resultDTO);
            }
            return result;
        } catch (ApiException e) {
            log.warn("[查询所有币种信息]异常，code={}，message={}", e.getCode(), e.getMessage());
            Throwable sourceCause = e.getCause();
            if (sourceCause instanceof IOException) {
                throw new ExchangeApiException(ExchangeResultCodeEnum.TIMEOUT);
            } else {
                throw new ExchangeApiException(ExchangeResultCodeEnum.FAIL, e);
            }
        }
    }


    /**
     * 查询市场深度信息
     */
    public ListOrderBookResultDTO listOrderBookCore(ListOrderBookRequestDTO requestDTO) throws ExchangeApiException {
        final SpotApi apiInstance = createSpotApi();
        try {
            OrderBook orderBook = apiInstance.listOrderBook(requestDTO.getMarket())
                    .interval(requestDTO.getInterval())
                    .limit(requestDTO.getLimit())
                    .withId(requestDTO.isWithId())
                    .execute();
            if(orderBook == null){
                return null;
            }

            List<List<String>> asks = orderBook.getAsks();
            List<List<String>> bids = orderBook.getBids();

            ListOrderBookResultDTO bookResultDTO = new ListOrderBookResultDTO();
            bookResultDTO.setAsks(new ArrayList<>());
            bookResultDTO.setBids(new ArrayList<>());
            for (List<String> ask : asks) {
                AskDTO askDTO = new AskDTO();
                askDTO.setPrice(new BigDecimal(ask.get(0)));
                askDTO.setTokenAmt(new BigDecimal(ask.get(1)));
                bookResultDTO.getAsks().add(askDTO);
            }
            for (List<String> bid : bids) {
                BidDTO bidDTO = new BidDTO();
                bidDTO.setPrice(new BigDecimal(bid.get(0)));
                bidDTO.setTokenAmt(new BigDecimal(bid.get(1)));
                bookResultDTO.getBids().add(bidDTO);
            }
            return bookResultDTO;
        } catch (ApiException e) {
            log.warn("[查询市场深度信息]异常，code={}，message={}", e.getCode(), e.getMessage());
            Throwable sourceCause = e.getCause();
            if (sourceCause instanceof IOException) {
                throw new ExchangeApiException(ExchangeResultCodeEnum.TIMEOUT);
            } else {
                throw new ExchangeApiException(ExchangeResultCodeEnum.FAIL, e);
            }
        }
    }


    /**
     * 查询现货账户
     */
    protected SpotAccountResultDTO spotAccountCore(SpotAccountRequestDTO request) throws ExchangeApiException {
        final SpotApi apiInstance = createSpotApi();
        try {
            List<SpotAccount> response = apiInstance.listSpotAccounts().currency(request.getCurrency())
                    .execute();
            SpotAccount account = response.get(0);
            SpotAccountResultDTO accountResultDTO = new SpotAccountResultDTO();
            accountResultDTO.setAvailable(new BigDecimal(Objects.requireNonNull(account.getAvailable())));
            accountResultDTO.setCurrency(account.getCurrency());
            accountResultDTO.setLocked(new BigDecimal(Objects.requireNonNull(account.getLocked())));
            return accountResultDTO;
        } catch (ApiException e) {
            log.warn("[查询现货账户]异常，code={}，message={}", e.getCode(), e.getMessage());
            Throwable sourceCause = e.getCause();
            if (sourceCause instanceof IOException) {
                throw new ExchangeApiException(ExchangeResultCodeEnum.TIMEOUT);
            } else {
                throw new ExchangeApiException(ExchangeResultCodeEnum.FAIL, e);
            }
        }
    }


    /**
     * 查询现货账户
     */
    protected List<SpotAccountResultDTO> listSpotAccountsCore() throws ExchangeApiException {
        final SpotApi apiInstance = createSpotApi();
        try {
            List<SpotAccountResultDTO> result = new ArrayList<>();
            List<SpotAccount> response = apiInstance.listSpotAccounts()
                    .execute();
            for (SpotAccount account : response) {
                SpotAccountResultDTO accountResultDTO = new SpotAccountResultDTO();
                accountResultDTO.setAvailable(new BigDecimal(Objects.requireNonNull(account.getAvailable())));
                accountResultDTO.setCurrency(account.getCurrency());
                accountResultDTO.setLocked(new BigDecimal(Objects.requireNonNull(account.getLocked())));
                result.add(accountResultDTO);
            }
            return result;
        } catch (ApiException e) {
            log.warn("[查询现货账户]异常，code={}，message={}", e.getCode(), e.getMessage());
            Throwable sourceCause = e.getCause();
            if (sourceCause instanceof IOException) {
                throw new ExchangeApiException(ExchangeResultCodeEnum.TIMEOUT);
            } else {
                throw new ExchangeApiException(ExchangeResultCodeEnum.FAIL, e);
            }
        }
    }


    /**
     * 取消现货触发订单
     */
    protected CancelSpotPriceTriggeredOrderResultDTO cancelSpotPriceTriggeredOrderCore(CancelSpotPriceTriggeredOrderRequestDTO requestDTO)
            throws ExchangeApiException {
        final SpotApi apiInstance = createSpotApi();
        try {
            apiInstance.cancelSpotPriceTriggeredOrder(requestDTO.getOrderId());
            return new CancelSpotPriceTriggeredOrderResultDTO();
        } catch (ApiException e) {
            log.warn("[取消现货触发订单]异常，code={}，message={}", e.getCode(), e.getMessage());
            Throwable sourceCause = e.getCause();
            if (sourceCause instanceof IOException) {
                throw new ExchangeApiException(ExchangeResultCodeEnum.TIMEOUT);
            } else {
                throw new ExchangeApiException(ExchangeResultCodeEnum.FAIL, e);
            }
        }
    }


    /**
     * 创建现货触发订单
     */
    protected SpotPriceTriggeredOrderResultDTO createSpotPriceTriggeredOrderCore(SpotPriceTriggeredOrderRequestDTO request)
            throws ExchangeApiException {

        final SpotApi apiInstance = createSpotApi();
        SpotPriceTriggeredOrder order = new SpotPriceTriggeredOrder();
        //币种
        order.setMarket(request.getMarket());
        //触发条件
        SpotPriceTrigger trigger = new SpotPriceTrigger();
        trigger.setPrice(request.getTriggerPrice().toString());
        trigger.setRule(RuleEnum.fromValue(request.getTriggerRule().getValue()));
        trigger.setExpiration(request.getTriggerExpiration());
        order.setTrigger(trigger);
        //委托条件
        SpotPricePutOrder putOrder = new SpotPricePutOrder();
        putOrder.setAccount(AccountEnum.NORMAL);
        putOrder.setSide(SpotPricePutOrder.SideEnum.fromValue(request.getSideEnum().getValue()));
        putOrder.setAmount(request.getTokenAmt().toString());
        putOrder.setPrice(request.getPrice().toString());
        order.setPut(putOrder);
        try {
            TriggerOrderResponse triggeredOrder = apiInstance.createSpotPriceTriggeredOrder(order);
            SpotPriceTriggeredOrderResultDTO orderResultDTO = new SpotPriceTriggeredOrderResultDTO();
            orderResultDTO.setOrderId(String.valueOf(triggeredOrder.getId()));
            return orderResultDTO;
        } catch (ApiException e) {
            log.warn("[创建触发订单]异常，code={}，message={}，order={}", e.getCode(), e.getMessage(),JSON.toJSONString(order));
            Throwable sourceCause = e.getCause();
            if (sourceCause instanceof IOException) {
                throw new ExchangeApiException(ExchangeResultCodeEnum.TIMEOUT);
            } else {
                throw new ExchangeApiException(ExchangeResultCodeEnum.FAIL, e);
            }
        }
    }


    /**
     * 查询单个订单详情
     */
    protected CreateSpotOrderResultDTO spotOderQueryCore(SpotOderQueryRequestDTO sourceRequest) throws ExchangeApiException {
        final SpotApi apiInstance = createSpotApi();
        try {
            Order response = apiInstance.getOrder(sourceRequest.getOrderId(), sourceRequest.getMarket(), StringUtils.EMPTY);
            CreateSpotOrderResultDTO resultDTO = new CreateSpotOrderResultDTO();
            resultDTO.setOrderId(response.getId());
            resultDTO.setOrderStatusEnum(OrderStatusEnum.toEnum(Objects.requireNonNull(response.getStatus()).getValue()));
            return resultDTO;
        } catch (ApiException e) {
            log.warn("[查询单个订单详情]异常，code={}，message={}，request={}", e.getCode(), e.getMessage(), JSON.toJSONString(sourceRequest));
            if (e.getCode() == 404 && e.getMessage().contains("ORDER_NOT_FOUND")) {
                throw new ExchangeApiException(ExchangeResultCodeEnum.ORDER_NOT_FOUND);
            }
            Throwable sourceCause = e.getCause();
            if (sourceCause instanceof IOException) {
                throw new ExchangeApiException(ExchangeResultCodeEnum.TIMEOUT);
            } else {
                throw new ExchangeApiException(ExchangeResultCodeEnum.FAIL);
            }
        }
    }

    /**
     * 现货下单
     */
    protected CreateSpotOrderResultDTO createSpotOrderCore(CreateSpotOrderRequestDTO request) throws ExchangeApiException {
        final SpotApi apiInstance = createSpotApi();
        Order order = new Order();
        try {
            order.setAccount(Order.AccountEnum.SPOT);
            order.setSide(Order.SideEnum.fromValue(request.getSideEnum().getValue()));
            order.setCurrencyPair(request.getMarket());
            order.setAmount(request.getTokenAmt().toString());
            order.setPrice(request.getPrice().toString());
            order.setText(request.getText());
            order.setTimeInForce(Order.TimeInForceEnum.fromValue(request.getTimeInForceEnum().getValue()));
            Order response = apiInstance.createOrder(order);

            log.info("[现货下单]结束，sourceRequest={}，response={}", JSON.toJSONString(request), JSON.toJSONString(response));

            CreateSpotOrderResultDTO resultDTO = new CreateSpotOrderResultDTO();
            resultDTO.setOrderId(response.getId());
            resultDTO.setOrderStatusEnum(OrderStatusEnum.toEnum(Objects.requireNonNull(response.getStatus()).getValue()));
            resultDTO.setFillTotal(new BigDecimal(response.getFilledTotal()));
            resultDTO.setFee(new BigDecimal(response.getFee()));
            return resultDTO;
        } catch (ApiException e) {
            log.warn("[现货下单]异常，code={}，message={}，order={}", e.getCode(), e.getMessage(), JSON.toJSONString(order));
            Throwable sourceCause = e.getCause();
            if (sourceCause instanceof IOException) {
                throw new ExchangeApiException(ExchangeResultCodeEnum.TIMEOUT);
            } else {
                throw new ExchangeApiException(ExchangeResultCodeEnum.FAIL);
            }
        }
    }


    /**
     * 查询蜡烛图
     */
    protected List<CandlestickResultDTO> listCandlesticksCore(CandlestickRequestDTO request) throws ExchangeApiException {
        final SpotApi apiInstance = createSpotApi();
        final APIlistCandlesticksRequest client = apiInstance
                .listCandlesticks(request.getMarket())
                .interval(request.getIntervalEnum().getValue());
        if (Objects.nonNull(request.getLimit())) {
            client.limit(request.getLimit());
        } else {
            client.from(DateFormatEnum.DATETIME_DEFAULT.parseToUtilDate(request.getFrom()).getTime() / 1000);
            client.to(DateFormatEnum.DATETIME_DEFAULT.parseToUtilDate(request.getTo()).getTime() / 1000);
        }
        try {
            List<CandlestickResultDTO> resultDTO = Lists.newArrayList();
            List<List<String>> response = client.execute();
            for (List<String> tickString : response) {
                String timestamp = tickString.get(0); //unix时间戳
                String volume = tickString.get(1);  //交易额，计价货币 USDT等
                String close = tickString.get(2); //收盘价
                String highest = tickString.get(3); //最高价
                String lowest = tickString.get(4); //最低价
                String open = tickString.get(5); //开盘价
                CandlestickResultDTO tickDTO = new CandlestickResultDTO();
                tickDTO.setMarket(request.getMarket());
                tickDTO.setVolumeString(volume);
                tickDTO.setCloseString(close);
                tickDTO.setHighestString(highest);
                tickDTO.setLowestString(lowest);
                tickDTO.setOpenString(open);
                tickDTO.setTimestampString(timestamp);
                tickDTO.setDateTimeString(DateFormatEnum.DATETIME_DEFAULT.formatTimestamp(timestamp));
                tickDTO.setDateTime(DateFormatEnum.DATETIME_DEFAULT.parseTimestampToLocalDatetime(timestamp));
                tickDTO.setVolume(new BigDecimal(volume));
                tickDTO.setClose(new BigDecimal(close));
                tickDTO.setHighest(new BigDecimal(highest));
                tickDTO.setLowest(new BigDecimal(lowest));
                tickDTO.setOpen(new BigDecimal(open));
                tickDTO.setClose(new BigDecimal(close));
                resultDTO.add(tickDTO);
            }
            return resultDTO;
        } catch (ApiException e) {
            log.warn("[查询蜡烛图]异常，code={}，message={}", e.getCode(), e.getMessage());
            Throwable sourceCause = e.getCause();
            if (sourceCause instanceof IOException) {
                throw new ExchangeApiException(ExchangeResultCodeEnum.TIMEOUT);
            } else {
                throw new ExchangeApiException(ExchangeResultCodeEnum.FAIL);
            }
        }
    }

    /**
     * 获取单个Ticker信息
     */
    protected TickResultDTO getTickerCore(TickRequestDTO requestDTO) throws ExchangeApiException {
        final SpotApi apiInstance = createSpotApi();
        try {
            final List<Ticker> tickers = apiInstance.listTickers().currencyPair(requestDTO.getMarket()).execute();
            if (CollectionUtils.isEmpty(tickers)) {
                return null;
            }
            Ticker ticker = tickers.get(0);
            TickResultDTO resultDTO = new TickResultDTO();
            resultDTO.setBaseVolume(new BigDecimal(Objects.requireNonNull(ticker.getBaseVolume())));
            resultDTO.setQuoteVolume(new BigDecimal(Objects.requireNonNull(ticker.getQuoteVolume())));
            resultDTO.setCurrencyPair(ticker.getCurrencyPair());
            resultDTO.setLast(new BigDecimal(Objects.requireNonNull(ticker.getLast())));
            resultDTO.setHigh24h(new BigDecimal(Objects.requireNonNull(ticker.getHigh24h())));
            resultDTO.setLow24h(new BigDecimal(Objects.requireNonNull(ticker.getLow24h())));
            resultDTO.setHighestBid(new BigDecimal(Objects.requireNonNull(ticker.getHighestBid())));
            resultDTO.setLowestAsk(new BigDecimal(Objects.requireNonNull(ticker.getLowestAsk())));
            resultDTO.setChangePercentage(ticker.getChangePercentage());
            return resultDTO;
        } catch (ApiException e) {
            log.warn("[获取单个Ticker信息]异常，code={}，message={}", e.getCode(), e.getMessage());
            Throwable sourceCause = e.getCause();
            if (sourceCause instanceof IOException) {
                throw new ExchangeApiException(ExchangeResultCodeEnum.TIMEOUT);
            } else {
                throw new ExchangeApiException(ExchangeResultCodeEnum.FAIL);
            }
        }
    }


    /**
     * 获取所有Ticker信息
     */
    protected List<TickResultDTO> getTickersCore() throws ExchangeApiException {
        final SpotApi apiInstance = createSpotApi();
        try {
            final List<Ticker> tickers = apiInstance.listTickers().execute();
            List<TickResultDTO> result = new ArrayList<>();
            for (Ticker ticker : tickers) {
                TickResultDTO resultDTO = new TickResultDTO();
                resultDTO.setBaseVolume(NullUtils.ifNullDefaultZero(ticker.getBaseVolume()));
                resultDTO.setCurrencyPair(ticker.getCurrencyPair());
                resultDTO.setHigh24h(NullUtils.ifNullDefaultZero(ticker.getHigh24h()));
                resultDTO.setHighestBid(NullUtils.ifNullDefaultZero(ticker.getHighestBid()));
                resultDTO.setLast(NullUtils.ifNullDefaultZero(ticker.getLast()));
                resultDTO.setLow24h(NullUtils.ifNullDefaultZero(ticker.getLow24h()));
                resultDTO.setLowestAsk(NullUtils.ifNullDefaultZero(ticker.getLowestAsk()));
                resultDTO.setQuoteVolume(NullUtils.ifNullDefaultZero(ticker.getQuoteVolume()));
                resultDTO.setChangePercentage(NullUtils.ifNullDefaultBlank(ticker.getChangePercentage()));
                result.add(resultDTO);
            }
            return result;
        } catch (ApiException e) {
            log.warn("[获取所有Ticker信息]异常，code={}，message={}", e.getCode(), e.getMessage());
            Throwable sourceCause = e.getCause();
            if (sourceCause instanceof IOException) {
                throw new ExchangeApiException(ExchangeResultCodeEnum.TIMEOUT);
            } else {
                throw new ExchangeApiException(ExchangeResultCodeEnum.FAIL);
            }
        }
    }

    /**
     * 创建现货API
     */
    private SpotApi createSpotApi() {
        ApiClient client = Configuration.getDefaultApiClient();
        client.setBasePath("https://api.gateio.ws/api/v4");
        client.setApiKeySecret(userSecretDTO.getKey(), userSecretDTO.getSecret());
        return new SpotApi(client);
    }
}
