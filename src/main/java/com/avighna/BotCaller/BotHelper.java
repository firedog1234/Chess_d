package com.avighna.BotCaller;

import com.avighna.Game.*;

public class BotHelper {
    public synchronized String buildFenString(Board board){
        //rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR
        String fen = "";
        int runningCount = 0;
        boolean flag = false;

        for(int i = 0; i < 8; i++){
            runningCount = 0;
            flag = false;
            for(int j = 0; j < 8; j++){
                if(board.getPiece(i,j).getSymbol().equals("♜")){
                    if(flag){
                        fen += String.valueOf(runningCount);
                        flag = false;
                    }
                    fen += "r";
                }
                else if(board.getPiece(i,j).getSymbol().equals("♖")){
                    if(flag){
                        fen += String.valueOf(runningCount);
                        flag = false;
                    }
                    fen += "R";
                }
                else if(board.getPiece(i,j).getSymbol().equals("♞")){
                    if(flag){
                        fen += String.valueOf(runningCount);
                        flag = false;
                    }
                    fen += "n";
                }
                else if(board.getPiece(i,j).getSymbol().equals("♘")){
                    if(flag){
                        fen += String.valueOf(runningCount);
                        flag = false;
                    }
                    fen += "N";
                }
                else if(board.getPiece(i,j).getSymbol().equals("♝")){
                    if(flag){
                        fen += String.valueOf(runningCount);
                        flag = false;
                    }
                    fen += "b";
                }
                else if(board.getPiece(i,j).getSymbol().equals("♗")){
                    if(flag){
                        fen += String.valueOf(runningCount);
                        flag = false;
                    }
                    fen += "B";
                }
                else if(board.getPiece(i,j).getSymbol().equals("♛")){
                    if(flag){
                        fen += String.valueOf(runningCount);
                        flag = false;
                    }
                    fen += "q";
                }
                else if(board.getPiece(i,j).getSymbol().equals("♕")){
                    if(flag){
                        fen += String.valueOf(runningCount);
                        flag = false;
                    }
                    fen += "Q";
                }
                else if(board.getPiece(i,j).getSymbol().equals("♚")){
                    if(flag){
                        fen += String.valueOf(runningCount);
                        flag = false;
                    }
                    fen += "k";
                }
                else if(board.getPiece(i,j).getSymbol().equals("♔")){
                    if(flag){
                        fen += String.valueOf(runningCount);
                        flag = false;
                    }
                    fen += "K";
                }
                else if(board.getPiece(i,j).getSymbol().equals("♟")){
                    if(flag){
                        fen += String.valueOf(runningCount);
                        flag = false;
                    }
                    fen += "p";
                }
                else if(board.getPiece(i,j).getSymbol().equals("♙")){
                    if(flag){
                        fen += String.valueOf(runningCount);
                        flag = false;
                    }
                    fen += "P";
                }
                else{
                    flag = true;
                    runningCount+=1;
                }

            }
            if (flag) {
                fen += String.valueOf(runningCount);
            }
            if (i < 7) {
                fen += "/";
            }

        }

        return fen;

    }

}

/*let runBoard = [
        ["♜", "♞", "♝", "♛", "♚", "♝", "♞", "♜"],
        ["♟", "♟", "♟", "♟", "♟", "♟", "♟", "♟"],
        ["",   "",   "",   "",   "",   "",   "",   ""  ],
        ["",   "",   "",   "",   "",   "",   "",   ""  ],
        ["",   "",   "",   "",   "",   "",   "",   ""  ],
        ["",   "",   "",   "",   "",   "",   "",   ""  ],
        ["♙", "♙", "♙", "♙", "♙", "♙", "♙", "♙"],
        ["♖", "♘", "♗", "♕", "♔", "♗", "♘", "♖"]
        ];*/
