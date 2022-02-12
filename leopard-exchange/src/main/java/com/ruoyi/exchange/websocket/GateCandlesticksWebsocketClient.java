package com.ruoyi.exchange.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.pleuvoir.common.model.dto.TickDTO;
import io.github.pleuvoir.common.model.enums.CandlesticksIntervalEnum;
import io.github.pleuvoir.common.utils.DateFormatEnum;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import lombok.Getter;

/**
 * 蜡烛图订阅
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
public abstract class GateCandlesticksWebsocketClient extends AbstractGateWebSocketClient {

    @Getter
    private String market;

    public GateCandlesticksWebsocketClient(CandlesticksIntervalEnum intervalEnum, String market) throws URISyntaxException {
        super("spot.candlesticks", new String[]{intervalEnum.getValue(), market});
        this.market = market;
    }

    @Override
    protected void onUpdate(String message) {

        JSONObject source = JSON.parseObject(message);

        String timestamp = source.getString("t"); //unix时间戳
        String volume = source.getString("v");  //交易额，计价货币 USDT等
        String close = source.getString("c"); //收盘价
        String highest = source.getString("h"); //最高价
        String lowest = source.getString("l"); //最低价
        String open = source.getString("o"); //开盘价
        String desc = source.getString("n"); //描述

        TickDTO tickDTO = new TickDTO();
        tickDTO.setMarket(this.getMarket());
        tickDTO.setVolumeString(volume);
        tickDTO.setCloseString(close);
        tickDTO.setHighestString(highest);
        tickDTO.setLowestString(lowest);
        tickDTO.setOpenString(open);
        tickDTO.setDesc(desc);
        tickDTO.setTimestampString(timestamp);
        tickDTO.setDateTimeString(DateFormatEnum.DATETIME_DEFAULT.formatTimestamp(timestamp));

        tickDTO.setDateTime(DateFormatEnum.DATETIME_DEFAULT.parseTimestampToLocalDatetime(timestamp));

        tickDTO.setVolume(new BigDecimal(volume));
        tickDTO.setClose(new BigDecimal(close));
        tickDTO.setHighest(new BigDecimal(highest));
        tickDTO.setLowest(new BigDecimal(lowest));
        tickDTO.setOpen(new BigDecimal(open));
        tickDTO.setClose(new BigDecimal(close));

        this.onTick(tickDTO);

    }


    protected abstract void onTick(TickDTO tickDTO);


}
