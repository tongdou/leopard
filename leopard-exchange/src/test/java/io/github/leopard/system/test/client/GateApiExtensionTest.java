package io.github.leopard.system.test.client;

import io.github.leopard.exchange.extension.GateApiExtension;
import io.github.leopard.exchange.model.dto.Result;
import io.github.leopard.exchange.model.dto.UserSecretDTO;
import io.github.leopard.exchange.model.dto.request.SpotPriceTriggeredOrderRequestDTO;
import io.github.leopard.exchange.model.dto.result.SpotPriceTriggeredOrderResultDTO;
import io.github.leopard.exchange.model.dto.result.TickResultDTO;
import org.junit.Test;

/**
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
public class GateApiExtensionTest {

    @Test
    public void createSpotPriceTriggeredOrder_test(){
        UserSecretDTO userSecret = new UserSecretDTO("", "");
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

}
