package com.avighna;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pair<F, S> {
  private static final Logger logger = LoggerFactory.getLogger(Pair.class);
  public F first;
  public S second;

  public Pair(F first, S second) {
    this.first = first;
    this.second = second;
  }

  public void logMove() {
    logger.info("here is the current move stored first: {}", this.first);
    logger.info("here is the current move stored second: {}", this.second);
  }
}
