package com.avighna.Game;

public class Bishop extends Piece{
    Bishop(String color){
        super(color.equals("white") ? "♗" : "♝" ,color);
    }

}
