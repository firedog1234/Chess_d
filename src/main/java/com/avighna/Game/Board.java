package com.avighna.Game;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private Piece[][] board;

    public Board(){
        this.board = new Piece[8][8];
        initializeBoard();
    }

    public Piece[][] getBoard(){
        return this.board;
    }

    public Piece getPiece(int row, int col){
        return board[row][col];
    }

    public void setPiece(int row, int col, Piece piece){
        board[row][col] = piece;
    }


    public void movePiece(int sourceRow, int sourceCol, int targetRow, int targetCol){
        this.setPiece(targetRow, targetCol, this.getPiece(sourceRow, sourceCol));
        this.setPiece(sourceRow, sourceCol, null);
    }

    public void printBoard(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board[i][j] != null){
                    System.out.print(board[i][j].getSymbol());
                }
                else{
                    System.out.print("_");
                }
            }
            System.out.println();
        }
    }

    private void initializeBoard(){
        board[0][0] = new Rook("black");
        board[0][1] = new Knight("black");
        board[0][2] = new Bishop("black");
        board[0][3] = new Queen("black");
        board[0][4] = new King("black");
        board[0][5] = new Bishop("black");
        board[0][6] = new Knight("black");
        board[0][7] = new Rook("black");

        for (int col = 0; col < 8; col++) {
            board[1][col] = new Pawn("black");
            board[6][col] = new Pawn("white");
        }

        board[7][0] = new Rook("white");
        board[7][1] = new Knight("white");
        board[7][2] = new Bishop("white");
        board[7][3] = new Queen("white");
        board[7][4] = new King("white");
        board[7][5] = new Bishop("white");
        board[7][6] = new Knight("white");
        board[7][7] = new Rook("white");

        for (int row = 2; row <= 5; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }
    }





}
