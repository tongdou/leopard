package io.github.leopard.system.test.client;

import io.github.leopard.exchange.extension.GateApiExtension;
import io.github.leopard.exchange.model.dto.Result;
import io.github.leopard.exchange.model.dto.UserSecretDTO;
import io.github.leopard.exchange.model.dto.request.SpotPriceTriggeredOrderRequestDTO;
import io.github.leopard.exchange.model.dto.result.SpotPriceTriggeredOrderResultDTO;

/**
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
public class GateApiExtensionTest {

    public static void main(String[] args) {

        UserSecretDTO userSecret = new UserSecretDTO("", "");

        GateApiExtension client = GateApiExtension.create(userSecret);

        SpotPriceTriggeredOrderRequestDTO requestDTO = new SpotPriceTriggeredOrderRequestDTO();
        Result<SpotPriceTriggeredOrderResultDTO> result = client.createSpotPriceTriggeredOrder(requestDTO);
        if (result.isSuccess()) {
            SpotPriceTriggeredOrderResultDTO data = result.getData();

        } else {
            Integer code = result.getCode();
        }
    }

}
