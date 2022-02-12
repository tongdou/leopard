package com.ruoyi.exchange.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.ToJSON;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractGateWebSocketClient extends WebSocketClient {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final AtomicBoolean shutdown = new AtomicBoolean(false);

    private final ScheduledExecutorService executors = Executors.newSingleThreadScheduledExecutor();

    @Getter
    private String subscribeEvent;
    @Getter
    private String[] payload;


    public AbstractGateWebSocketClient(String subscribeEvent, String[] payload) throws URISyntaxException {
        super(new URI("wss://api.gateio.ws/ws/v4/"));
        this.addHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
        this.addHeader("Connection", "Upgrade");
        this.addHeader("Upgrade", "websocket");
        this.subscribeEvent = subscribeEvent;
        this.payload = payload;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        if (shutdown.get()) {
            close();
            return;
        }
        this.sendHeartPacketPeriodicity();
    }


    @SneakyThrows
    public AbstractGateWebSocketClient subscribe() {
        connectBlocking();
        send(WebSocketMessageDTO.builder()
                .channel(this.getSubscribeEvent())
                .payload(this.getPayload())
                .event("subscribe")
                .build()
                .ToJSON());
        log.info("{}subscribe ok, receive now.", Arrays.asList(this.payload));
        return this;
    }


    @Override
    public void onMessage(String message) {
        JSONObject source = JSON.parseObject(message);
        if (source.getString("event").equals("update")) {
            this.onUpdate(source.getString("result"));
        }
    }

    protected abstract void onUpdate(String message);


    @Override
    public void onClose(int i, String s, boolean b) {
    }

    @Override
    public void onError(Exception e) {
        log.error("gate websocket error .... ", e);
    }

    public void shutdownGracefully() {
        if (!this.shutdown.compareAndSet(false, true)) {
            log.warn("this websocket channel has already closed.");
            return;
        }
        if (!getReadyState().equals(ReadyState.CLOSED)) {
            try {
                closeBlocking();
                executors.shutdown();
            } catch (InterruptedException ignore) {
            }
        }
    }


    private void sendHeartPacketPeriodicity() {
        this.executors.schedule(() -> {
            if (isOpen()) {
                send(WebSocketMessageDTO.builder().channel("spot.ping").build().ToJSON());
                this.sendHeartPacketPeriodicity(); // 递归这个任务 数秒后继续发送，不用关闭连接池
            }
            //用户手动终止
            if (shutdown.get()) {
                return;
            }

            //其它情况中重连
            if (getReadyState().equals(ReadyState.NOT_YET_CONNECTED)) {
                try {
                    connectBlocking();
                } catch (IllegalStateException | InterruptedException ignored) {
                }
            } else if (getReadyState().equals(ReadyState.CLOSED)) {
                try {
                    reconnectBlocking();
                } catch (InterruptedException ignore) {
                }
            }
        }, 10, TimeUnit.SECONDS);
    }

    @Data
    @Builder
    private static class WebSocketMessageDTO implements ToJSON {

        private Long time = System.currentTimeMillis() / 1000;
        private String channel;
        private String event;
        private String[] payload;

    }


}