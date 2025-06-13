package com.avighna.Server;

import com.avighna.APP.App;
import com.avighna.Json.JsonHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import com.avighna.Game.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetSocketAddress;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChessWebSocketServer extends WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public ChessWebSocketServer(InetSocketAddress address){
        super(address);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

        webSocket.send("you connected");
        new Thread(new GameSession(webSocket)).start();
        logger.info("game session: {}", webSocket.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        logger.info("game session closed: {}", webSocket.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        logger.info("message recieved {}", s);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root;
        try {
            root = objectMapper.readTree(s);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonHolder holder = new JsonHolder();

        holder.setSourceRow(root.get("sourceRow").asInt());
        holder.setSourceCol(root.get("sourceCol").asInt());
        holder.setTargetRow(root.get("targetRow").asInt());
        holder.setTargetCol(root.get("targetCol").asInt());

    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {

    }
}
