package com.avighna.Game;
import com.avighna.APP.App;
import org.java_websocket.WebSocket;
import com.avighna.Json.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameSession implements Runnable {



    //run the game stuff;
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private final WebSocket connection;
    private Board board;
    public GameSession(WebSocket connection){
        this.connection = connection;
    }

    public boolean validateAndMove(JsonHolder holder){
        int sourceRow = holder.getSourceRow();
        int sourceColumn = holder.getSourceCol();
        int targetRow = holder.getTargetRow();
        int targetColumn = holder.getTargetCol();

        boolean isValid = board.getPiece(sourceRow, sourceColumn).validateMove(sourceRow, sourceColumn, targetRow, targetColumn);
        board.movePiece(sourceRow,sourceColumn,targetRow,targetColumn);

        return isValid;

    }

    public void run(){
        connection.send("you just connected i think");
        logger.info("run is ran");

        this.board = new Board();



    }

}
