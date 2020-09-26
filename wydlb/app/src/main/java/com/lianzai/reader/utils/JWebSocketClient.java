package com.lianzai.reader.utils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;

public class JWebSocketClient extends WebSocketClient {
    public JWebSocketClient(URI serverUri) {
        super(serverUri, new Draft_6455());
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        super.onMessage(bytes);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        RxLogTool.e("JWebSocketClient", "onOpen()");
    }

    @Override
    public void onMessage(String message) {
        RxLogTool.e("JWebSocketClient", "onMessage()");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        RxLogTool.e("JWebSocketClient", "onClose()");
    }

    @Override
    public void onError(Exception ex) {
        RxLogTool.e("JWebSocketClient", "onError()");
    }
}
