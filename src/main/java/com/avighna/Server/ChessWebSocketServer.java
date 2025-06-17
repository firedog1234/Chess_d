package com.avighna.Server;

import com.avighna.APP.App;
import com.avighna.Json.JsonHolder;
import com.avighna.Game.*;
import com.avighna.Manager.GameManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

import java.util.HashMap;
import java.util.Map;


public class ChessWebSocketServer extends WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public ChessWebSocketServer(InetSocketAddress address){
        super(address);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        GameSession gameSession = new GameSession(webSocket);
        GameManager.addGameSession(webSocket, gameSession);
        Thread gameThread = new Thread(gameSession);
        gameThread.start();
        webSocket.send("you connected");
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

        Map<String, Object> response = new HashMap<>();

        GameManager.getGameSession(webSocket);
        //write a .validate for a game session reagrding the board ig idk we have the json holder so we can prolly do sum
        if(GameManager.getGameSession(webSocket).validateAndMove(holder)){
            response.put("status", "OK");
            response.put("sourceRow", holder.getSourceRow());
            response.put("sourceColumn", holder.getSourceCol());
            response.put("targetRow", holder.getTargetRow());
            response.put("targetColumn", holder.getTargetCol());

            ObjectMapper mapping =  new ObjectMapper();

            try {
                String jsonWebSocketSend = objectMapper.writeValueAsString(response);
                webSocket.send(jsonWebSocketSend);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
        else{
            webSocket.send("NO");
        }

    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {

    }
}
