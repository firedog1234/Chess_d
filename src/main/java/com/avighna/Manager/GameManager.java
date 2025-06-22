package com.avighna.Manager;

import java.util.concurrent.ConcurrentHashMap;

import com.avighna.APP.App;
import com.avighna.Game.GameSession;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameManager {

    private static final Logger logger = LoggerFactory.getLogger(GameManager.class);
    private static ConcurrentHashMap<WebSocket, GameSession> games = new ConcurrentHashMap<>();

    public static void addGameSession(WebSocket webSocket, GameSession gameSession){
        games.put(webSocket, gameSession);

    }

    public static GameSession getGameSession(WebSocket webSocket){
        return games.get(webSocket);
    }

    public static void removeGameSession(WebSocket webSocket){
        games.remove(webSocket);
    }

}
