package com.avighna.BotCaller;

import com.avighna.Game.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class providing helper methods for chess bot operations This class contains static
 * utility methods for board analysis and move conversion
 */
public class BotHelper {

  private static final Logger logger = LoggerFactory.getLogger(BotHelper.class);

  // Private constructor to prevent instantiation of utility class
  private BotHelper() {
    throw new UnsupportedOperationException(
        "BotHelper is a utility class and cannot be instantiated");
  }

  /**
   * Builds a FEN (Forsyth-Edwards Notation) string from the current board state This is a
   * thread-safe version of the FEN builder
   *
   * @param board The current chess board
   * @return FEN string representing the board position
   * @throws IllegalArgumentException if board is null
   */
  public static synchronized String buildFenString(Board board) {
    if (board == null) {
      throw new IllegalArgumentException("Board cannot be null");
    }

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

    logger.debug("Generated FEN string: {}", fen.toString());
    return fen.toString();
  }

  /**
   * Converts a piece to its FEN notation symbol
   *
   * @param piece The chess piece
   * @return FEN symbol for the piece
   * @throws IllegalArgumentException if piece is null or has unknown type
   */
  public static String getFenSymbol(Piece piece) {
    if (piece == null) {
      throw new IllegalArgumentException("Piece cannot be null");
    }

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
   * Converts algebraic notation (e.g., "e4") to board position
   *
   * @param algebraic The algebraic notation (e.g., "e4", "a1")
   * @return Position object representing the square
   * @throws IllegalArgumentException if notation is invalid
   */
  public static Piece.Position algebraicToPosition(String algebraic) {
    if (algebraic == null || algebraic.length() != 2) {
      throw new IllegalArgumentException("Invalid algebraic notation: " + algebraic);
    }

    char file = Character.toLowerCase(algebraic.charAt(0));
    char rank = algebraic.charAt(1);

    if (file < 'a' || file > 'h' || rank < '1' || rank > '8') {
      throw new IllegalArgumentException("Invalid algebraic notation: " + algebraic);
    }

    int col = file - 'a';
    int row = 8 - (rank - '0'); // Convert rank to 0-based row (rank 8 = row 0)

    return new Piece.Position(row, col);
  }

  /**
   * Converts board position to algebraic notation
   *
   * @param position The board position
   * @return Algebraic notation string (e.g., "e4")
   * @throws IllegalArgumentException if position is null
   */
  public static String positionToAlgebraic(Piece.Position position) {
    if (position == null) {
      throw new IllegalArgumentException("Position cannot be null");
    }

    char file = (char) ('a' + position.col());
    char rank = (char) ('8' - position.row()); // Convert 0-based row to rank

    return "" + file + rank;
  }

  /**
   * Parses a move string from Stockfish format (e.g., "e2e4") into positions
   *
   * @param moveString The move string in UCI format
   * @return Array containing [fromPosition, toPosition]
   * @throws IllegalArgumentException if move string is invalid
   */
  public static Piece.Position[] parseUciMove(String moveString) {
    if (moveString == null || moveString.length() < 4) {
      throw new IllegalArgumentException("Invalid UCI move string: " + moveString);
    }

    String fromSquare = moveString.substring(0, 2);
    String toSquare = moveString.substring(2, 4);

    Piece.Position from = algebraicToPosition(fromSquare);
    Piece.Position to = algebraicToPosition(toSquare);

    return new Piece.Position[] {from, to};
  }

  /**
   * Converts a move to UCI format string
   *
   * @param from Starting position
   * @param to Ending position
   * @return UCI format move string (e.g., "e2e4")
   * @throws IllegalArgumentException if positions are null
   */
  public static String moveToUci(Piece.Position from, Piece.Position to) {
    if (from == null || to == null) {
      throw new IllegalArgumentException("Positions cannot be null");
    }

    return positionToAlgebraic(from) + positionToAlgebraic(to);
  }

  /**
   * Validates if a position is within board bounds
   *
   * @param position The position to validate
   * @return true if position is valid, false otherwise
   */
  public static boolean isValidPosition(Piece.Position position) {
    if (position == null) {
      return false;
    }

    return position.row() >= 0 && position.row() < 8 && position.col() >= 0 && position.col() < 8;
  }

  /**
   * Gets all pieces of a specific color from the board
   *
   * @param board The chess board
   * @param color The piece color to search for
   * @return Map of positions to pieces for the specified color
   * @throws IllegalArgumentException if board or color is null
   */
  public static Map<Piece.Position, Piece> getPiecesByColor(Board board, Piece.PieceColor color) {
    if (board == null) {
      throw new IllegalArgumentException("Board cannot be null");
    }
    if (color == null) {
      throw new IllegalArgumentException("Color cannot be null");
    }

    Map<Piece.Position, Piece> pieces = new HashMap<>();

    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        Piece.Position position = new Piece.Position(row, col);
        Piece piece = board.getPiece(position);

        if (piece != null && piece.getColor() == color) {
          pieces.put(position, piece);
        }
      }
    }

    return pieces;
  }

  /**
   * Finds the king position for a given color
   *
   * @param board The chess board
   * @param color The color of the king to find
   * @return Position of the king, or null if not found
   * @throws IllegalArgumentException if board or color is null
   */
  public static Piece.Position findKingPosition(Board board, Piece.PieceColor color) {
    if (board == null) {
      throw new IllegalArgumentException("Board cannot be null");
    }
    if (color == null) {
      throw new IllegalArgumentException("Color cannot be null");
    }

    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        Piece.Position position = new Piece.Position(row, col);
        Piece piece = board.getPiece(position);

        if (piece != null && piece.getType() == Piece.PieceType.KING && piece.getColor() == color) {
          return position;
        }
      }
    }

    logger.warn("King not found for color: {}", color);
    return null;
  }

  /**
   * Counts the number of pieces of each type for a given color
   *
   * @param board The chess board
   * @param color The piece color to count
   * @return Map of piece types to their counts
   * @throws IllegalArgumentException if board or color is null
   */
  public static Map<Piece.PieceType, Integer> countPiecesByType(
      Board board, Piece.PieceColor color) {
    if (board == null) {
      throw new IllegalArgumentException("Board cannot be null");
    }
    if (color == null) {
      throw new IllegalArgumentException("Color cannot be null");
    }

    Map<Piece.PieceType, Integer> counts = new EnumMap<>(Piece.PieceType.class);

    // Initialize all counts to 0
    for (Piece.PieceType type : Piece.PieceType.values()) {
      counts.put(type, 0);
    }

    // Count pieces
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        Piece.Position position = new Piece.Position(row, col);
        Piece piece = board.getPiece(position);

        if (piece != null && piece.getColor() == color) {
          counts.put(piece.getType(), counts.get(piece.getType()) + 1);
        }
      }
    }

    return counts;
  }

  /**
   * Calculates material value for a given color Standard piece values: Pawn=1, Knight=3, Bishop=3,
   * Rook=5, Queen=9, King=0
   *
   * @param board The chess board
   * @param color The piece color to evaluate
   * @return Total material value
   * @throws IllegalArgumentException if board or color is null
   */
  public static int calculateMaterialValue(Board board, Piece.PieceColor color) {
    Map<Piece.PieceType, Integer> counts = countPiecesByType(board, color);

    int value = 0;
    value += counts.get(Piece.PieceType.PAWN);
    value += counts.get(Piece.PieceType.KNIGHT) * 3;
    value += counts.get(Piece.PieceType.BISHOP) * 3;
    value += counts.get(Piece.PieceType.ROOK) * 5;
    value += counts.get(Piece.PieceType.QUEEN) * 9;
    // King has no material value

    return value;
  }
}
