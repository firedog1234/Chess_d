package com.avighna.Game;

public class Pawn extends Piece{
    Pawn(String color){
        super(color.equals("white") ? "♙" : "♟" ,color);
    }

    public boolean validateMove(int sourceRow, int SourceColumn, int targetRow, int targetColumn){
        return true;
    }

}
