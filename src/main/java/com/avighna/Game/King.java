package com.avighna.Game;

public class King extends Piece {
  public King(PieceColor color) {
    super(PieceType.KING, color);
  }

  @Override
  public boolean isValidMove(Position from, Position to, Board board) {
    return true;
  }
}
