package com.avighna.App;

import com.avighna.Game.*;
import com.avighna.Server.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

  private static final Logger logger = LoggerFactory.getLogger(App.class);

  public static void main(String[] args) throws IOException {

    ChessHttpServer httpServer = new ChessHttpServer();
    ChessWebSocketServer webSocket =
        new ChessWebSocketServer(new InetSocketAddress("localhost", 8081));
    GameSession.setServer(webSocket);

    httpServer.initializeServer();
    logger.info("HTTP Server Started");
    webSocket.start();
    logger.info("WebSocket started");
  }
}
