package Chess.util;
/**
 * Constants – enumerations used throughout the chess application.
 */
public class Constants {

    private Constants() {} // utility class

    // -------------------------------------------------------------------------
    // Colour constants
    // -------------------------------------------------------------------------
    public static final int WHITE = 0;
    public static final int BLACK = 1;

    // -------------------------------------------------------------------------
    // Game mode
    // -------------------------------------------------------------------------
    public enum GameMode {
        PVP,  // Player vs Player
        PVB   // Player vs Bot
    }

    // -------------------------------------------------------------------------
    // Game status
    // -------------------------------------------------------------------------
    public enum GameStatus {
        ONGOING,
        CHECK,
        CHECKMATE,
        STALEMATE,
        DRAW,
        RESIGNED
    }

    // -------------------------------------------------------------------------
    // Bot difficulty
    // -------------------------------------------------------------------------
    public enum Difficulty {
        EASY   (1),
        MEDIUM (3),
        HARD   (5);

        public final int depth;
        Difficulty(int depth) { this.depth = depth; }
    }

    // -------------------------------------------------------------------------
    // Piece types
    // -------------------------------------------------------------------------
    public enum PieceType {
        KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN
    }
}
