package com.avighna.Game;

public class Piece {
    private String symbol;
    private String color;

    Piece(String symbol, String color){
        this.symbol = symbol;
        this.color = color;
    }
    public String getColor() {
        return color;
    }

    public String getSymbol(){
        return symbol;
    }
}
