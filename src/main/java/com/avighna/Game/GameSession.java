package com.avighna.Game;
import com.avighna.APP.App;
import org.java_websocket.WebSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameSession implements Runnable {

    //run the game stuff;
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private final WebSocket connection;
    public GameSession(WebSocket connection){
        this.connection = connection;
    }

    public void run(){
        connection.send("you just connected i think");
        logger.info("run is ran");

        Board board = new Board();


    }

}
