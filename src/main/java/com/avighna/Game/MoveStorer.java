package com.avighna.Game;

import com.avighna.Pair;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveStorer {

  private static final Logger logger = LoggerFactory.getLogger(MoveStorer.class);

  private String bestMove;
  private boolean engineRan = false;

  private Pair<Integer, Integer> startingSquare;

  private Pair<Integer, Integer> endingSquare;

  public boolean isEngineRan() {
    return engineRan;
  }

  public void setEngineRan(boolean engineRan) {
    this.engineRan = engineRan;
  }

  public synchronized String getBestMove() {
    return bestMove;
  }

  public Pair<Integer, Integer> getStartingSquare() {
    return startingSquare;
  }

  public void setStartingSquare(Pair<Integer, Integer> startingSquare) {
    this.startingSquare = startingSquare;
  }

  public Pair<Integer, Integer> getEndingSquare() {
    return endingSquare;
  }

  public void setEndingSquare(Pair<Integer, Integer> endingSquare) {
    this.endingSquare = endingSquare;
  }

  public synchronized void setBestMove(String bestMove) {
    this.bestMove = bestMove.substring(9, 13);
    ;
    convertMove(this.bestMove);
    startingSquare.logMove();
    endingSquare.logMove();
    logger.info("about to notify");
    notify();
    logger.info("notify complete");
  }

  public synchronized void waitForMove() {
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

  public void convertMove(String bestMove) {
    HashMap<String, Integer> alphaMap = new HashMap<>();
    alphaMap.put("a", 0);
    alphaMap.put("b", 1);
    alphaMap.put("c", 2);
    alphaMap.put("d", 3);
    alphaMap.put("e", 4);
    alphaMap.put("f", 5);
    alphaMap.put("g", 6);
    alphaMap.put("h", 7);

    Integer fromRow;
    Integer fromCol;
    Integer toRow;
    Integer toCol;

    fromRow = Integer.parseInt(bestMove.substring(1, 2));
    fromCol = alphaMap.get(bestMove.substring(0, 1));

    toRow = Integer.parseInt(bestMove.substring(3));
    toCol = alphaMap.get(bestMove.substring(2, 3));

    setStartingSquare(new Pair<Integer, Integer>(fromRow, fromCol));
    setEndingSquare(new Pair<Integer, Integer>(toRow, toCol));
  }
}
