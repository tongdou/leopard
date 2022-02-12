package io.github.leopard.exchange.extension;

import io.github.leopard.exchange.client.GateApi;
import io.github.leopard.exchange.exception.ExchangeApiException;
import io.github.leopard.exchange.model.dto.Result;
import io.github.leopard.exchange.model.dto.UserSecretDTO;
import io.github.leopard.exchange.model.dto.request.CreateSpotOrderRequestDTO;
import io.github.leopard.exchange.model.dto.request.SpotPriceTriggeredOrderRequestDTO;
import io.github.leopard.exchange.model.dto.result.CreateSpotOrderResultDTO;
import io.github.leopard.exchange.model.dto.result.SpotPriceTriggeredOrderResultDTO;

/**
 * GATE交易
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
public class GateApiExtension extends GateApi {

    private GateApiExtension(UserSecretDTO userSecretDTO) {
        super(userSecretDTO);
    }

    public static GateApiExtension create(UserSecretDTO userSecretDTO) {
        return new GateApiExtension(userSecretDTO);
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

}
