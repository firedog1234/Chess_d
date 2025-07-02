package com.avighna.Game;

import com.avighna.Game.Piece.PieceColor;

// Factory class for creating pieces
public class PieceFactory {
  // Convenience methods
  public static Piece whitePawn() {
    return new Pawn(PieceColor.WHITE);
  }

  public static Piece blackPawn() {
    return new Pawn(PieceColor.BLACK);
  }

  public static Piece whiteRook() {
    return new Rook(PieceColor.WHITE);
  }

  public static Piece blackRook() {
    return new Rook(PieceColor.BLACK);
  }

  public static Piece whiteKnight() {
    return new Knight(PieceColor.WHITE);
  }

  public static Piece blackKnight() {
    return new Knight(PieceColor.BLACK);
  }

  public static Piece whiteBishop() {
    return new Bishop(PieceColor.WHITE);
  }

  public static Piece blackBishop() {
    return new Bishop(PieceColor.BLACK);
  }

  public static Piece whiteQueen() {
    return new Queen(PieceColor.WHITE);
  }

  public static Piece blackQueen() {
    return new Queen(PieceColor.BLACK);
  }

  public static Piece whiteKing() {
    return new King(PieceColor.WHITE);
  }

  public static Piece blackKing() {
    return new King(PieceColor.BLACK);
  }
}
