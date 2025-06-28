package com.avighna.Game;

import com.avighna.Manager.GameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class MoveStorer {

    private static final Logger logger = LoggerFactory.getLogger(MoveStorer.class);

    private String bestMove;
    private boolean engineRan = false;

    public boolean isEngineRan() {
        return engineRan;
    }

    public void setEngineRan(boolean engineRan) {
        this.engineRan = engineRan;
    }

    public synchronized String getBestMove() {
        return bestMove;
    }

    public synchronized void setBestMove(String bestMove) {
        this.bestMove = bestMove.substring(9,13);;
        logger.info("about to notify");
        notify();
        logger.info("notify complete");
    }

    public synchronized void waitForMove(){
        while (!this.isEngineRan()) {
            try {
                logger.info("wating for best move response from stockfish");
                wait();
                logger.info("wait for best move response is complete");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void convertMove(String bestMove){
        HashMap<String, Integer> alphaMap = new HashMap<>();
        alphaMap.put("a", 0);
        alphaMap.put("b", 1);
        alphaMap.put("c", 2);
        alphaMap.put("d", 3);
        alphaMap.put("e", 4);
        alphaMap.put("f", 5);
        alphaMap.put("g", 6);
        alphaMap.put("h", 7);




    }


}
