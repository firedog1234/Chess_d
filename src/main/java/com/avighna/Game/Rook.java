package com.avighna.Game;

public class Rook extends Piece {
  public Rook(PieceColor color) {
    super(PieceType.ROOK, color);
  }

  @Override
  public boolean isValidMove(Position from, Position to, Board board) {
    return true;
  }
}
