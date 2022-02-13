package io.github.leopard.system.test.client;

import com.alibaba.fastjson.JSON;
import io.github.leopard.exchange.extension.GateApiExtension;
import io.github.leopard.exchange.model.dto.ExchangeUserSecretDTO;
import io.github.leopard.exchange.model.dto.Result;
import io.github.leopard.exchange.model.dto.request.CandlestickRequestDTO;
import io.github.leopard.exchange.model.dto.request.EatSpotOrderMarketRequestDTO;
import io.github.leopard.exchange.model.dto.request.PrevCandlestickRequestDTO;
import io.github.leopard.exchange.model.dto.request.SpotOderQueryRequestDTO;
import io.github.leopard.exchange.model.dto.request.SpotPriceTriggeredOrderRequestDTO;
import io.github.leopard.exchange.model.dto.result.CandlestickResultDTO;
import io.github.leopard.exchange.model.dto.result.CreateSpotOrderResultDTO;
import io.github.leopard.exchange.model.dto.result.CurrencyPairResultDTO;
import io.github.leopard.exchange.model.dto.result.EatSpotOrderMarketResultDTO;
import io.github.leopard.exchange.model.dto.result.SpotAccountResultDTO;
import io.github.leopard.exchange.model.dto.result.SpotPriceTriggeredOrderResultDTO;
import io.github.leopard.exchange.model.dto.result.TickResultDTO;
import io.github.leopard.exchange.model.enums.CandlesticksIntervalEnum;
import io.github.leopard.exchange.model.enums.OrderStatusEnum;
import io.github.leopard.exchange.model.enums.SideEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.Test;

/**
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
public class GateApiExtensionTest {



    @Test
    public void spotOderQueryMust_test() {

        ExchangeUserSecretDTO userSecret = new ExchangeUserSecretDTO("", "");
        GateApiExtension client = GateApiExtension.auth(userSecret);

        SpotOderQueryRequestDTO queryRequestDTO = new SpotOderQueryRequestDTO();
        queryRequestDTO.setMarket("SLP_USDT");
        queryRequestDTO.setOrderId("123812903276");
        CreateSpotOrderResultDTO createSpotOrderResultDTO = client.spotOderQueryMustOrNull(queryRequestDTO);

        OrderStatusEnum orderStatusEnum = createSpotOrderResultDTO.getOrderStatusEnum();

        System.out.println(orderStatusEnum.equals(OrderStatusEnum.CLOSED));

        System.out.println();
        System.out.println(createSpotOrderResultDTO.ToJSON());
    }



    @Test
    public void eatSpotOrderMarketMust_test() {

        ExchangeUserSecretDTO userSecret = new ExchangeUserSecretDTO("", "");
        GateApiExtension client = GateApiExtension.auth(userSecret);

        EatSpotOrderMarketRequestDTO marketRequestDTO = new EatSpotOrderMarketRequestDTO();
        marketRequestDTO.setSideEnum(SideEnum.BUY);
        marketRequestDTO.setUsdtAmt(new BigDecimal(2));
        marketRequestDTO.setMarket("SLP_USDT");
        marketRequestDTO.setText("你大爷的");

        EatSpotOrderMarketResultDTO resultDTO = client.eatSpotOrderMarketMustOrNull(marketRequestDTO);
        System.out.println(resultDTO.ToJSON());

        SpotOderQueryRequestDTO queryRequestDTO = new SpotOderQueryRequestDTO();
        queryRequestDTO.setMarket("SLP_USDT");
        queryRequestDTO.setOrderId(resultDTO.getOrderId());
        CreateSpotOrderResultDTO createSpotOrderResultDTO = client.spotOderQueryMustOrNull(queryRequestDTO);

        System.out.println(createSpotOrderResultDTO.ToJSON());

    }


    @Test
    public void createSpotPriceTriggeredOrder_test() {
        ExchangeUserSecretDTO userSecret = new ExchangeUserSecretDTO("", "");
        GateApiExtension client = GateApiExtension.auth(userSecret);
        SpotPriceTriggeredOrderRequestDTO requestDTO = new SpotPriceTriggeredOrderRequestDTO();
        Result<SpotPriceTriggeredOrderResultDTO> result = client.createSpotPriceTriggeredOrder(requestDTO);
        if (result.isSuccess()) {
            SpotPriceTriggeredOrderResultDTO data = result.getData();
        } else {
            Integer code = result.getCode();
        }
    }


    @Test
    public void getTicker_test() {
        GateApiExtension client = GateApiExtension.create();
        TickResultDTO resultDTO = client.getTickerMust("BTC_USDT");
        System.out.println(resultDTO.ToJSON());
    }

    @Test
    public void listCurrencyPairsMust_test() {
        GateApiExtension client = GateApiExtension.create();
        List<CurrencyPairResultDTO> resultDTOS = client.listCurrencyPairsMust();
        System.out.println(JSON.toJSONString(resultDTOS, true));
    }

    @Test
    public void currencyPairsMust_test() {
        GateApiExtension client = GateApiExtension.create();
        CurrencyPairResultDTO resultDTOS = client.currencyPairsMust("BTC_USDT");
        System.out.println(JSON.toJSONString(resultDTOS, true));
    }


    @Test
    public void getTickerMust_test() {
        GateApiExtension client = GateApiExtension.create();
        List<TickResultDTO> tickerMust = client.getTickerMust();
        System.out.println(JSON.toJSONString(tickerMust, true));
    }

    @Test
    public void spotAccountMust_test() {
        ExchangeUserSecretDTO userSecret = new ExchangeUserSecretDTO("", "");

        GateApiExtension client = GateApiExtension.auth(userSecret);
        SpotAccountResultDTO resultDTO = client.spotAccountMust("USDT");
        System.out.println(JSON.toJSONString(resultDTO, true));
    }


    @Test
    public void listCandlestickMust_test() {
        GateApiExtension client = GateApiExtension.create();
        CandlestickRequestDTO requestDTO = new CandlestickRequestDTO();
        requestDTO.setMarket("BTC_USDT");
        requestDTO.setIntervalEnum(CandlesticksIntervalEnum.M_5);
        requestDTO.setLimit(1);
        List<CandlestickResultDTO> resultDTOS = client.listCandlestickMust(requestDTO);
        System.out.println(JSON.toJSONString(resultDTOS, true));
    }


    @Test
    public void prevCandlestickMust_test() {
        GateApiExtension client = GateApiExtension.create();
        PrevCandlestickRequestDTO requestDTO = new PrevCandlestickRequestDTO();
        requestDTO.setMarket("BTC_USDT");
        requestDTO.setIntervalEnum(CandlesticksIntervalEnum.M_5);
        requestDTO.setCurDateTime(LocalDateTime.now());
        CandlestickResultDTO resultDTO = client.prevCandlestickMust(requestDTO);
        System.out.println(JSON.toJSONString(resultDTO, true));
    }
}
