package io.github.leopard.system.test.client;

import io.github.leopard.exchange.model.dto.result.CandlestickResultDTO;
import io.github.leopard.exchange.model.enums.CandlesticksIntervalEnum;
import io.github.leopard.exchange.client.websocket.GateCandlesticksWebsocketClient;
import java.net.URISyntaxException;

/**
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
public class GateCandlesticksWebsocketClientTest {

    public static void main(String[] args) throws URISyntaxException {

        new GateCandlesticksWebsocketClient(CandlesticksIntervalEnum.S_10, "BTC_USDT") {

            @Override
            protected void onTick(CandlestickResultDTO candlestickResultDTO) {
                System.out.println(candlestickResultDTO.ToJSON());
            }
        }.subscribe();


    }

}
