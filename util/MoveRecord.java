package Chess.util;

import Chess.model.board.Cell;
import Chess.model.pieces.Piece;

/**
 * MoveRecord – immutable snapshot of a single move for history and undo support.
 */
public class MoveRecord {

    public final Cell  from;
    public final Cell  to;
    public final Piece movedPiece;
    public final Piece capturedPiece; // null if no capture
    public final boolean isCastle;
    public final boolean isEnPassant;
    public final boolean isPromotion;
    public final String  notation;   // Algebraic notation (e.g. "e4", "Nf3", "O-O")

    // Saved board state for undo (coordinates of from/to before the move)
    public final int fromX, fromY, toX, toY;
    public final boolean pieceHadMoved;

    public MoveRecord(Cell from, Cell to, Piece movedPiece, Piece capturedPiece,
                      boolean isCastle, boolean isEnPassant, boolean isPromotion,
                      String notation, boolean pieceHadMoved) {
        this.from          = from;
        this.to            = to;
        this.movedPiece    = movedPiece;
        this.capturedPiece = capturedPiece;
        this.isCastle      = isCastle;
        this.isEnPassant   = isEnPassant;
        this.isPromotion   = isPromotion;
        this.notation      = notation;
        this.fromX         = from.getX();
        this.fromY         = from.getY();
        this.toX           = to.getX();
        this.toY           = to.getY();
        this.pieceHadMoved = pieceHadMoved;
    }

    @Override
    public String toString() {
        return notation;
    }
}
