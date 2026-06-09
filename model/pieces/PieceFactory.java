package chess.model.pieces;

import chess.model.PieceColor;
import chess.model.PieceType;

/**
 * Factory class for creating chess pieces.
 *
 * <p>Centralises piece construction so the rest of the codebase
 * doesn't need to know which concrete subclass to instantiate.</p>
 */
public class PieceFactory {

    private PieceFactory() {} // utility class — no instances

    /**
     * Create a new piece of the given type and color.
     */
    public static Piece create(PieceType type, PieceColor color) {
        switch (type) {
            case PAWN:   return new Pawn(color);
            case KNIGHT: return new Knight(color);
            case BISHOP: return new Bishop(color);
            case ROOK:   return new Rook(color);
            case QUEEN:  return new Queen(color);
            case KING:   return new King(color);
            default: throw new IllegalArgumentException("Unknown piece type: " + type);
        }
    }

    /**
     * Deep-copy an existing piece, preserving its hasMoved state.
     */
    public static Piece copy(Piece piece) {
        if (piece == null) return null;
        return piece.copy();
    }
}
