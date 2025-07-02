package com.avighna.Server;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class ChessHttpServer {
  public void initializeServer() throws IOException {
    HttpServer chessHttpServer = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);

    chessHttpServer.createContext("/", new FrontServerHandler());
    chessHttpServer.setExecutor(Executors.newFixedThreadPool(10));

    chessHttpServer.start();
  }
}
