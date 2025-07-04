package com.avighna.BotCaller;

import com.avighna.Game.*;
import com.avighna.Pair;
import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotCaller implements Runnable {

  private static final Logger logger = LoggerFactory.getLogger(BotCaller.class);

  /**
   * Builds a FEN (Forsyth-Edwards Notation) string from the current board state
   *
   * @param board The current chess board
   * @return FEN string representing the board position
   */
  public String buildFenString(Board board) {
    StringBuilder fen = new StringBuilder();

    for (int row = 0; row < 8; row++) {
      int emptySquares = 0;

      for (int col = 0; col < 8; col++) {
        Piece.Position position = new Piece.Position(row, col);
        Piece piece = board.getPiece(position);

        if (piece == null) {
          emptySquares++;
        } else {
          // Add empty square count if any
          if (emptySquares > 0) {
            fen.append(emptySquares);
            emptySquares = 0;
          }

          // Add piece symbol
          fen.append(getFenSymbol(piece));
        }
      }

      // Add remaining empty squares at end of row
      if (emptySquares > 0) {
        fen.append(emptySquares);
      }

      // Add row separator (except for last row)
      if (row < 7) {
        fen.append("/");
      }
    }

    return fen.toString();
  }

  /**
   * Converts a piece to its FEN notation symbol
   *
   * @param piece The chess piece
   * @return FEN symbol for the piece
   */
  private String getFenSymbol(Piece piece) {
    String symbol;

    switch (piece.getType()) {
      case PAWN:
        symbol = "p";
        break;
      case ROOK:
        symbol = "r";
        break;
      case KNIGHT:
        symbol = "n";
        break;
      case BISHOP:
        symbol = "b";
        break;
      case QUEEN:
        symbol = "q";
        break;
      case KING:
        symbol = "k";
        break;
      default:
        throw new IllegalArgumentException("Unknown piece type: " + piece.getType());
    }

    // Uppercase for white pieces, lowercase for black
    return piece.getColor() == Piece.PieceColor.WHITE ? symbol.toUpperCase() : symbol;
  }

  /**
   * Placeholder method for API calls (not currently used)
   *
   * @return placeholder response
   */
  public String callApi() {
    return "hello";
  }

  @Override
  public synchronized void run() {
    logger.info("BotCaller thread started");

    while (true) {
      try {
        if (HoldingQueue.getSize() > 0) {
          logger.info("BotCaller processing request from queue");
          processStockfishRequest();
        } else {
          // Sleep when no requests to process
          Thread.sleep(100);
        }
      } catch (InterruptedException e) {
        logger.warn("BotCaller thread interrupted");
        Thread.currentThread().interrupt();
        break;
      } catch (Exception e) {
        logger.error("Error in BotCaller thread", e);
        // Continue running even if individual request fails
      }
    }

    logger.info("BotCaller thread terminated");
  }

  /** Processes a single Stockfish request from the queue */
  private void processStockfishRequest() {
    Pair<Board, MoveStorer> request = HoldingQueue.dequeueRequest();
    if (request == null) {
      logger.warn("Dequeued null request");
      return;
    }

    Board board = request.first;
    MoveStorer moveStorer = request.second;

    try {
      String bestMove = getStockfishMove(board);
      moveStorer.setBestMove(bestMove);
      logger.info("Stockfish returned best move: {}", bestMove);
    } catch (Exception e) {
      logger.error("Failed to get move from Stockfish", e);
      // Set a default/error response
      moveStorer.setBestMove("bestmove (none)");
    }
  }

  /**
   * Gets the best move from Stockfish engine
   *
   * @param board Current board state
   * @return Stockfish response containing best move
   * @throws IOException if communication with Stockfish fails
   */
  private String getStockfishMove(Board board) throws IOException {
    ProcessBuilder processBuilder = new ProcessBuilder("stockfish");
    Process stockfishProcess = null;

    try {
      stockfishProcess = processBuilder.start();

      try (BufferedWriter writer =
              new BufferedWriter(new OutputStreamWriter(stockfishProcess.getOutputStream()));
          BufferedReader reader =
              new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()))) {

        // Send commands to Stockfish
        sendStockfishCommands(writer, board);

        // Read response from Stockfish
        return readStockfishResponse(reader);
      }
    } finally {
      if (stockfishProcess != null) {
        stockfishProcess.destroyForcibly();
      }
    }
  }

  /**
   * Sends the necessary commands to Stockfish
   *
   * @param writer Output stream to Stockfish
   * @param board Current board state
   * @throws IOException if writing fails
   */
  private void sendStockfishCommands(BufferedWriter writer, Board board) throws IOException {
    writer.write("uci\n");
    writer.write("position fen " + buildFenString(board) + " b - - 0 1\n");
    writer.write("go depth 10\n");
    writer.flush();
  }

  /**
   * Reads the best move response from Stockfish
   *
   * @param reader Input stream from Stockfish
   * @return The line containing the best move
   * @throws IOException if reading fails
   */
  private String readStockfishResponse(BufferedReader reader) throws IOException {
    String line;
    while ((line = reader.readLine()) != null) {
      if (line.startsWith("bestmove")) {
        return line;
      }
    }

    // If we get here, Stockfish didn't return a best move
    logger.warn("Stockfish did not return a best move");
    return "bestmove (none)";
  }
}
