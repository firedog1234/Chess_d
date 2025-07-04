package com.avighna.Game;

import java.util.Objects;

public abstract class Piece {
  private final PieceColor color;
  private final PieceType type;
  private boolean hasMoved = false;

  protected Piece(PieceType type, PieceColor color) {
    this.type = Objects.requireNonNull(type, "Type cannot be null");
    this.color = Objects.requireNonNull(color, "Color cannot be null");
  }

  // Getters
  public PieceColor getColor() {
    return color;
  }

  public PieceType getType() {
    return type;
  }

  public String getSymbol() {
    return type.getSymbol(color);
  }

  public boolean hasMoved() {
    return hasMoved;
  }

  // Mark piece as moved
  public void markAsMoved() {
    this.hasMoved = true;
  }

  // Abstract method that each piece implementation must provide
  public abstract boolean isValidMove(Position from, Position to, Board board);

  // Helper methods available to all pieces
  protected boolean isValidPosition(Position pos) {
    return pos.row() >= 0 && pos.row() < 8 && pos.col() >= 0 && pos.col() < 8;
  }

  protected boolean isPathClear(Position from, Position to, Board board) {
    int rowDelta = Integer.signum(to.row() - from.row());
    int colDelta = Integer.signum(to.col() - from.col());

    int currentRow = from.row() + rowDelta;
    int currentCol = from.col() + colDelta;

    while (currentRow != to.row() || currentCol != to.col()) {
      if (board.getPiece(new Position(currentRow, currentCol)) != null) {
        return false;
      }
      currentRow += rowDelta;
      currentCol += colDelta;
    }
    return true;
  }

  protected boolean canCaptureOrMoveToEmpty(Position to, Board board) {
    Piece targetPiece = board.getPiece(to);
    return targetPiece == null || targetPiece.getColor() != this.color;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Piece piece = (Piece) obj;
    return color == piece.color && type == piece.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, type);
  }

  @Override
  public String toString() {
    return color + " " + type;
  }

  public enum PieceColor {
    WHITE,
    BLACK;

    public PieceColor opposite() {
      return this == WHITE ? BLACK : WHITE;
    }
  }

  // Piece type enum with symbols but no movement logic
  public enum PieceType {
    PAWN("♙", "♟"),
    ROOK("♖", "♜"),
    KNIGHT("♘", "♞"),
    BISHOP("♗", "♝"),
    QUEEN("♕", "♛"),
    KING("♔", "♚");

    private final String whiteSymbol;
    private final String blackSymbol;

    PieceType(String whiteSymbol, String blackSymbol) {
      this.whiteSymbol = whiteSymbol;
      this.blackSymbol = blackSymbol;
    }

    public String getSymbol(PieceColor color) {
      return color == PieceColor.WHITE ? whiteSymbol : blackSymbol;
    }
  }

  public record Position(int row, int col) {
    public Position {
      if (row < 0 || row > 7 || col < 0 || col > 7) {
        throw new IllegalArgumentException("Invalid position: (" + row + ", " + col + ")");
      }
    }

    @Override
    public String toString() {
      return "(" + row + ", " + col + ")";
    }
  }
}
