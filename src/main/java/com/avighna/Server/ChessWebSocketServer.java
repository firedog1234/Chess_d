package com.avighna.Server;

import com.avighna.BotCaller.*;
import com.avighna.Game.*;
import com.avighna.Json.JsonHolder;
import com.avighna.Manager.GameManager;
import com.avighna.Pair;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChessWebSocketServer extends WebSocketServer {
  private static final Logger logger = LoggerFactory.getLogger(ChessWebSocketServer.class);

  public void sendBlackMove(
      Pair<Integer, Integer> startingSquare,
      Pair<Integer, Integer> endingSquare,
      WebSocket webSocket)
      throws JsonProcessingException {
    JsonHolder jsonHolder = new JsonHolder();
    HashMap<String, Integer> sendMap = new HashMap<>();
    ObjectMapper objectMapper = new ObjectMapper();
    GameManager.getGameSession(webSocket);

    sendMap.put("blacksMove", null);
    sendMap.put("rowFrom", startingSquare.first);
    sendMap.put("colFrom", startingSquare.second);
    sendMap.put("rowTo", endingSquare.first);
    sendMap.put("colTo", endingSquare.second);

    String jsonWebSocketSend = objectMapper.writeValueAsString(sendMap);
    webSocket.send(jsonWebSocketSend);
  }

  public ChessWebSocketServer(InetSocketAddress address) {
    super(address);
  }

  @Override
  public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
    GameSession gameSession = new GameSession(webSocket);
    GameManager.addGameSession(webSocket, gameSession);
    Thread gameThread = new Thread(gameSession);
    BotCaller stockfish = new BotCaller();
    Thread stockFishThread = new Thread(stockfish);
    stockFishThread.setName("stockFishThread");
    gameThread.start();
    stockFishThread.start();
    webSocket.send("you connected");
    logger.info("game session: {}", webSocket.getRemoteSocketAddress());
  }

  @Override
  public void onClose(WebSocket webSocket, int i, String s, boolean b) {
    GameManager.removeGameSession(webSocket);
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
    // write a .validate for a game session reagrding the board ig idk we have the json holder so we
    // can prolly do sum
    if (GameManager.getGameSession(webSocket).validateAndMove(holder)) {
      response.put("status", "OK");
      response.put("sourceRow", holder.getSourceRow());
      response.put("sourceColumn", holder.getSourceCol());
      response.put("targetRow", holder.getTargetRow());
      response.put("targetColumn", holder.getTargetCol());

      ObjectMapper mapping = new ObjectMapper();

      try {
        String jsonWebSocketSend = objectMapper.writeValueAsString(response);
        webSocket.send(jsonWebSocketSend);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }

    } else {
      webSocket.send("NO");
    }
  }

  @Override
  public void onError(WebSocket webSocket, Exception e) {}

  @Override
  public void onStart() {}
}
