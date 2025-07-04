package com.avighna.Game;

public class Bishop extends Piece {
  public Bishop(PieceColor color) {
    super(PieceType.BISHOP, color);
  }

  @Override
  public boolean isValidMove(Position from, Position to, Board board) {
    return true;
  }
}
