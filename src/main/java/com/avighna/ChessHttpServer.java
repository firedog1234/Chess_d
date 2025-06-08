package com.avighna;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.io.OutputStream;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

public class ChessHttpServer{
    private static final Logger logger = LoggerFactory.getLogger(ChessHttpServer.class);
    public void initializeServer() throws IOException{
        logger.info("Initializing server");

        HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
        httpServer.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String response = "Hello, World!";

                exchange.sendResponseHeaders(200, response.getBytes().length);

                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());

                logger.info("hello hello hello");
            }
        });

        httpServer.start();
        logger.info("server on");
    }
}
