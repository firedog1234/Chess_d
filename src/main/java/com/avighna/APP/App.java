package com.avighna.APP;
import com.avighna.Server.*;
import com.avighna.Game.*;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) throws IOException {

        ChessHTTPServer HTTPserver = new ChessHTTPServer();
        ChessWebSocketServer webSocket = new ChessWebSocketServer(new InetSocketAddress("localhost", 8081));
        GameSession.setServer(webSocket);

        HTTPserver.initializeServer();
        logger.info("HTTP Server Started");
        webSocket.start();
        logger.info("WebSocket started");


    }
}