package chess.model.board;

import chess.model.PieceColor;

/**
 * Tracks castling rights for both sides.
 *
 * <p>A castling right is lost when:
 * <ul>
 *   <li>The king of that color moves.</li>
 *   <li>The rook on that side moves or is captured.</li>
 * </ul>
 * </p>
 */
public class CastlingRights {

    private boolean whiteKingside;
    private boolean whiteQueenside;
    private boolean blackKingside;
    private boolean blackQueenside;

    public CastlingRights() {
        whiteKingside = whiteQueenside = blackKingside = blackQueenside = true;
    }

    /** Copy constructor. */
    public CastlingRights(CastlingRights other) {
        this.whiteKingside  = other.whiteKingside;
        this.whiteQueenside = other.whiteQueenside;
        this.blackKingside  = other.blackKingside;
        this.blackQueenside = other.blackQueenside;
    }

    public boolean canCastleKingside(PieceColor color) {
        return color == PieceColor.WHITE ? whiteKingside : blackKingside;
    }

    public boolean canCastleQueenside(PieceColor color) {
        return color == PieceColor.WHITE ? whiteQueenside : blackQueenside;
    }

    public void revokeKingside(PieceColor color) {
        if (color == PieceColor.WHITE) whiteKingside  = false;
        else                           blackKingside  = false;
    }

    public void revokeQueenside(PieceColor color) {
        if (color == PieceColor.WHITE) whiteQueenside = false;
        else                           blackQueenside = false;
    }

    public void revokeBoth(PieceColor color) {
        revokeKingside(color);
        revokeQueenside(color);
    }
}
