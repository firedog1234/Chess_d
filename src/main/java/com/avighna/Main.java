package com.avighna;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {


        ChessHttpServer server = new ChessHttpServer();
        server.initializeServer();



    }
}