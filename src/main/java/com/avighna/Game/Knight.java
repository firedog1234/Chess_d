package com.avighna.Game;

public class Knight extends Piece {
  public Knight(PieceColor color) {
    super(PieceType.KNIGHT, color);
  }

  @Override
  public boolean isValidMove(Position from, Position to, Board board) {
    return true;
  }
}
