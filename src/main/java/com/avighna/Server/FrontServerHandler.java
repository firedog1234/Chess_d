package com.avighna.Server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;

public class FrontServerHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String path = exchange.getRequestURI().getPath();

    switch (path) {
      case "/", "/index.html" -> serveTheFile("index.html", exchange);
      case "/move.js" -> serveTheFile("move.js", exchange);
      case "/style.css" -> serveTheFile("style.css", exchange);
    }
  }

  private void serveTheFile(String fileName, HttpExchange exchange) throws IOException {
    String theExtension = getTheExtension(fileName);

    // Load file from classpath resources
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static/" + fileName);

    if (inputStream == null) {
      // Send 404 if file not found
      String notFound = "File not found: " + fileName;
      exchange.sendResponseHeaders(404, notFound.length());
      exchange.getResponseBody().write(notFound.getBytes());
      exchange.getResponseBody().close();
      return;
    }

    byte[] theBytesToLoad = inputStream.readAllBytes();
    inputStream.close();

    exchange.getResponseHeaders().set("Content-Type", "text/" + theExtension);
    exchange.sendResponseHeaders(200, theBytesToLoad.length);
    exchange.getResponseBody().write(theBytesToLoad);
    exchange.getResponseBody().close();
  }

  private String getTheExtension(String fileName) {
    int index = fileName.indexOf('.');
    String theExtension = fileName.substring(index + 1);
    return theExtension;
  }
}
