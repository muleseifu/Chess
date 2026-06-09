package chess.model.board;

import chess.model.Move;
import chess.model.PieceColor;
import chess.model.Position;
import chess.model.pieces.Piece;

/**
 * Read-only interface for the chess board.
 * Used by piece move-generators so they only need
 * what they can observe, not the full mutable board.
 */
public interface ChessBoard {

    /** Returns the piece at the given position, or null if empty. */
    Piece getPieceAt(Position pos);

    /** Returns the last move played (for en passant detection). */
    Move getLastMove();

    /** Returns the current castling rights. */
    CastlingRights getCastlingRights();

    /**
     * Returns true if the given square is attacked by any piece of {@code byColor}.
     * Used for castling legality checks inside King.
     */
    boolean isSquareAttackedBy(Position pos, PieceColor byColor);
}
