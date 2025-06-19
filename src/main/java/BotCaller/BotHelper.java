package BotCaller;

import com.avighna.Game.*;

public class BotHelper {
    public synchronized String buildFenString(Board board){
        //rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR
        String fen = "";

        for(int i = 0; i < 8; i++){
            int runningCount = 0;
            for(int j = 0; j < 8; j++){
                if(board.getPiece(i,j).getSymbol().equals("♜")){
                    fen += "r";
                }
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
