package com.avighna.BotCaller;

import com.avighna.APP.App;
import com.avighna.Game.*;
import com.avighna.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class BotCaller implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(BotCaller.class);

    public String buildFenString(Board board){
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

    public String callApi(){
        return "hello";
    }



    @Override
    public void run(){
        logger.info("bot caller is running\n");
        if(HoldingQueue.getSize() > 0) {
            logger.info("borcaller is gonna deque\n");
            Pair<Board, MoveStorer> pair = HoldingQueue.dequeueRequest();

            ProcessBuilder pb = new ProcessBuilder("stockfish");
            Process process = null;
            try {
                process = pb.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

            try {
                out.write("uci\n");
                out.flush();
                out.write(buildFenString(pair.first) + " b - - 0 1");
                out.write("go depth 10");
                out.flush();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String reading;

            while (true) {
                try {
                    if (in.readLine() != null){
                        if(in.readLine().startsWith("bestmove")){
                            reading = in.readLine();
                            break;
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            logger.info(reading);

            pair.second.setBestMove(reading);




        }


    }
}
