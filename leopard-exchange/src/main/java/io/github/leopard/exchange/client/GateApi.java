package io.github.leopard.exchange.client;

import io.gate.gateapi.ApiClient;
import io.gate.gateapi.ApiException;
import io.gate.gateapi.Configuration;
import io.gate.gateapi.api.SpotApi;
import io.gate.gateapi.models.Order;
import io.gate.gateapi.models.SpotPricePutOrder;
import io.gate.gateapi.models.SpotPricePutOrder.AccountEnum;
import io.gate.gateapi.models.SpotPriceTrigger;
import io.gate.gateapi.models.SpotPriceTrigger.RuleEnum;
import io.gate.gateapi.models.SpotPriceTriggeredOrder;
import io.github.leopard.exchange.exception.ExchangeApiException;
import io.github.leopard.exchange.exception.ExchangeResultCodeEnum;
import io.github.leopard.exchange.model.dto.UserSecretDTO;
import io.github.leopard.exchange.model.dto.request.CreateSpotOrderRequestDTO;
import io.github.leopard.exchange.model.dto.request.SpotPriceTriggeredOrderRequestDTO;
import io.github.leopard.exchange.model.dto.result.CreateSpotOrderResultDTO;
import io.github.leopard.exchange.model.dto.result.SpotPriceTriggeredOrderResultDTO;
import io.github.leopard.exchange.model.enums.OrderStatusEnum;
import java.io.IOException;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GATE交易
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
public class GateApi {


    private final Logger log = LoggerFactory.getLogger(getClass());

    private UserSecretDTO userSecretDTO;

    protected GateApi(UserSecretDTO userSecretDTO) {
        this.userSecretDTO = userSecretDTO;
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
            apiInstance.createSpotPriceTriggeredOrder(order);
            return new SpotPriceTriggeredOrderResultDTO();
        } catch (ApiException e) {
            log.warn("[创建触发订单]异常，code={}，message={}", e.getCode(), e.getMessage());
            Throwable sourceCause = e.getCause();
            if (sourceCause instanceof IOException) {
                throw new ExchangeApiException(ExchangeResultCodeEnum.TIMEOUT);
            } else {
                throw new ExchangeApiException(ExchangeResultCodeEnum.FAIL, e);
            }
        }
    }

    /**
     * 挂现货单
     */
    protected CreateSpotOrderResultDTO createSpotOrderCore(CreateSpotOrderRequestDTO request) throws ExchangeApiException {
        final SpotApi apiInstance = createSpotApi();
        try {
            Order order = new Order();
            order.setAccount(Order.AccountEnum.SPOT);
            order.setSide(Order.SideEnum.fromValue(request.getSideEnum().getValue()));
            order.setCurrencyPair(request.getMarket());
            order.setAmount(request.getTokenAmt().toString());
            order.setPrice(request.getPrice().toString());
            order.setAccount(Order.AccountEnum.SPOT);
            order.setText(request.getText());
            order.setTimeInForce(Order.TimeInForceEnum.fromValue(request.getTimeInForceEnum().getValue()));
            Order response = apiInstance.createOrder(order);

            CreateSpotOrderResultDTO resultDTO = new CreateSpotOrderResultDTO();
            resultDTO.setOrderStatusEnum(OrderStatusEnum.toEnum(Objects.requireNonNull(response.getStatus()).getValue()));
            return resultDTO;
        } catch (ApiException e) {
            log.warn("[挂现货单]异常，code={}，message={}", e.getCode(), e.getMessage());
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
