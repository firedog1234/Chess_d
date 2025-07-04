package com.avighna.Manager;

import com.avighna.Game.GameSession;
import java.util.concurrent.ConcurrentHashMap;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameManager {

  private static final Logger logger = LoggerFactory.getLogger(GameManager.class);
  private static ConcurrentHashMap<WebSocket, GameSession> games = new ConcurrentHashMap<>();

  public static void addGameSession(WebSocket webSocket, GameSession gameSession) {
    logger.info("adding a gamesession");
    games.put(webSocket, gameSession);
  }

  public static GameSession getGameSession(WebSocket webSocket) {
    return games.get(webSocket);
  }

  public static void removeGameSession(WebSocket webSocket) {
    logger.info("removing a game session");
    games.remove(webSocket);
  }
}
