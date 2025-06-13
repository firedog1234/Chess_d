package com.avighna.Server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FrontServerHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException  {
        String path = exchange.getRequestURI().getPath();

        if(path.equals("/") || path.equals("/index.html")){
            serveTheFile("index.html", exchange);
        } else if (path.equals("/move.js")) {
            serveTheFile("move.js", exchange);
        } else if (path.equals("/style.css")) {
            serveTheFile("style.css", exchange);
        }


    }

    private void serveTheFile(String lastTwo, HttpExchange exchange) throws IOException {
        String theExtenstion = getTheExtension(lastTwo);
        File file = new File("/Users/avipatel/IdeaProjects/Chess_d/src/main/java/com/avighna/FrontEnd/" + lastTwo);
        byte[] theBytesToLoad = Files.readAllBytes(file.toPath());
        exchange.getResponseHeaders().set("Content-Type", "text/" + theExtenstion); //the Content type of the file will be the extension thing yk
        exchange.sendResponseHeaders(200, theBytesToLoad.length); // OK code and send the header over
        exchange.getResponseBody().write(theBytesToLoad); // write the file to the body of the response of the server when u connect waht is the stuff gon respond with
        exchange.getResponseBody().close();
    }

    private String getTheExtension(String lastTwo){
        int index = lastTwo.indexOf('.');
        String theExtenstion = lastTwo.substring(index + 1);

        return theExtenstion;
    }




}
