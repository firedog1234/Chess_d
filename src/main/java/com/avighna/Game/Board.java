package com.avighna.Game;

import static com.avighna.Game.PieceFactory.blackBishop;
import static com.avighna.Game.PieceFactory.blackKing;
import static com.avighna.Game.PieceFactory.blackKnight;
import static com.avighna.Game.PieceFactory.blackQueen;
import static com.avighna.Game.PieceFactory.blackRook;
import static com.avighna.Game.PieceFactory.whiteBishop;
import static com.avighna.Game.PieceFactory.whiteKing;
import static com.avighna.Game.PieceFactory.whiteKnight;
import static com.avighna.Game.PieceFactory.whiteQueen;
import static com.avighna.Game.PieceFactory.whiteRook;

public class Board {
  private Piece[][] board;

  public Board() {
    this.board = new Piece[8][8];
    initializeBoard();
  }

  public Piece[][] getBoard() {
    return this.board;
  }

  public Piece getPiece(Piece.Position position) {
    return board[position.row()][position.col()];
  }

  public void setPiece(int row, int col, Piece piece) {
    board[row][col] = piece;
  }

  public void movePiece(int sourceRow, int sourceCol, int targetRow, int targetCol) {
    this.setPiece(targetRow, targetCol, this.getPiece(new Piece.Position(sourceRow, sourceCol)));
    this.setPiece(sourceRow, sourceCol, null);
  }

  public void printBoard() {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (board[i][j] != null) {
          System.out.print(board[i][j].getSymbol());
        } else {
          System.out.print("_");
        }
      }
      System.out.println();
    }
  }

  private void initializeBoard() {
    board[0][0] = blackRook();
    board[0][1] = blackKnight();
    board[0][2] = blackBishop();
    board[0][3] = blackQueen();
    board[0][4] = blackKing();
    board[0][5] = blackBishop();
    board[0][6] = blackKnight();
    board[0][7] = blackRook();

    for (int col = 0; col < 8; col++) {
      board[1][col] = new Pawn(Piece.PieceColor.BLACK);
      board[6][col] = new Pawn(Piece.PieceColor.WHITE);
    }

    board[7][0] = whiteRook();
    board[7][1] = whiteKnight();
    board[7][2] = whiteBishop();
    board[7][3] = whiteQueen();
    board[7][4] = whiteKing();
    board[7][5] = whiteBishop();
    board[7][6] = whiteKnight();
    board[7][7] = whiteRook();

    for (int row = 2; row <= 5; row++) {
      for (int col = 0; col < 8; col++) {
        board[row][col] = null;
      }
    }
  }
}
