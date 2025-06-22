package com.avighna.Game;

import com.avighna.Manager.GameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        this.bestMove = bestMove;
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


}
