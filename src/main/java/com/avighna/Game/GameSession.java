package com.avighna.Game;
import com.avighna.APP.App;
import com.avighna.Pair;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.java_websocket.WebSocket;
import com.avighna.Json.*;
import com.avighna.Server.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameSession implements Runnable {



    //run the game stuff;
    private static final Logger logger = LoggerFactory.getLogger(GameSession.class);

    private final WebSocket connection;
    private static ChessWebSocketServer server;
    private boolean isBlacksMove = false;
    private Board board;
    public GameSession(WebSocket connection){
        this.connection = connection;
        this.board = new Board();
    }

    public static void setServer(ChessWebSocketServer server) {
        GameSession.server = server;
    }
    public static ChessWebSocketServer getServer(){
        return GameSession.server;
    }

    public WebSocket getConnection() {
        return connection;
    }

    public boolean validateAndMove(JsonHolder holder){
        int sourceRow = holder.getSourceRow();
        int sourceColumn = holder.getSourceCol();
        int targetRow = holder.getTargetRow();
        int targetColumn = holder.getTargetCol();

        boolean isValid = board.getPiece(sourceRow, sourceColumn).validateMove(sourceRow, sourceColumn, targetRow, targetColumn);
        board.movePiece(sourceRow,sourceColumn,targetRow,targetColumn);
        setBlacksMove(true);


        board.printBoard();

        logger.info(this.isBlacksMove() + "");

        return isValid;

    }

    public void run(){
        connection.send("you just connected i think");
        logger.info("run is ran");

        MoveStorer store = new MoveStorer();



        while (true) {
            if (this.isBlacksMove()) {
                logger.info("it is blacks move\n");
                Pair<Board, MoveStorer> p = new Pair<>(board, store);
                HoldingQueue.addRequest(p);
                setBlacksMove(false);
                logger.info("about to wait for a best move response from stockfish\n");
                store.waitForMove();
                logger.info("best move: " + p.second.getBestMove());
                store.setEngineRan(false);
                try {
                    GameSession.getServer().sendBlackMove(store.getStartingSquare(), store.getEndingSquare(), this.getConnection());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                board.movePiece(8 - store.getStartingSquare().first, store.getStartingSquare().second, 8 - store.getEndingSquare().first, store.getEndingSquare().second);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }




    }

    public boolean isBlacksMove() {
        return isBlacksMove;
    }

    public void setBlacksMove(boolean blacksMove) {
        isBlacksMove = blacksMove;
    }
}
