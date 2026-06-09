package chess.model.pieces;

import chess.model.Move;
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;
import chess.model.board.ChessBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * The Pawn piece.
 *
 * <p>Movement rules:
 * <ul>
 *   <li>Moves one square forward (in its color's direction).</li>
 *   <li>May move two squares forward from its starting rank.</li>
 *   <li>Captures diagonally one square forward.</li>
 *   <li>En passant: may capture an adjacent pawn that just moved two squares.</li>
 *   <li>Promotion: when reaching the last rank, promotes to Queen/Rook/Bishop/Knight.</li>
 * </ul>
 * </p>
 */
public class Pawn extends Piece {

    /** White pawns move up the board (decreasing row index). */
    private static final int WHITE_DIRECTION = -1;
    /** Black pawns move down the board (increasing row index). */
    private static final int BLACK_DIRECTION =  1;

    private static final int WHITE_START_ROW  = 6;
    private static final int BLACK_START_ROW  = 1;
    private static final int WHITE_PROMO_ROW  = 0;
    private static final int BLACK_PROMO_ROW  = 7;

    private static final PieceType[] PROMOTION_TYPES = {
        PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT
    };

    public Pawn(PieceColor color) {
        super(PieceType.PAWN, color);
    }

    private Pawn(Pawn other) {
        super(other);
    }

    @Override
    public Piece copy() {
        return new Pawn(this);
    }

    @Override
    public List<Move> generatePseudoLegalMoves(Position from, ChessBoard board) {
        List<Move> moves = new ArrayList<>();
        int dir = (getColor() == PieceColor.WHITE) ? WHITE_DIRECTION : BLACK_DIRECTION;
        int startRow = (getColor() == PieceColor.WHITE) ? WHITE_START_ROW : BLACK_START_ROW;
        int promoRow = (getColor() == PieceColor.WHITE) ? WHITE_PROMO_ROW : BLACK_PROMO_ROW;

        addForwardMoves(moves, from, board, dir, startRow, promoRow);
        addCaptureMoves(moves, from, board, dir, promoRow);
        addEnPassantMoves(moves, from, board, dir);

        return moves;
    }

    private void addForwardMoves(List<Move> moves, Position from, ChessBoard board,
                                 int dir, int startRow, int promoRow) {
        Position oneAhead = from.offset(dir, 0);
        if (!oneAhead.isValid() || board.getPieceAt(oneAhead) != null) return;

        if (oneAhead.row == promoRow) {
            addPromotionMoves(moves, from, oneAhead, null);
        } else {
            moves.add(new Move(from, oneAhead, this, null));

            // Double push from start row
            if (from.row == startRow) {
                Position twoAhead = from.offset(dir * 2, 0);
                if (board.getPieceAt(twoAhead) == null) {
                    moves.add(new Move(from, twoAhead, this, null));
                }
            }
        }
    }

    private void addCaptureMoves(List<Move> moves, Position from, ChessBoard board,
                                 int dir, int promoRow) {
        for (int dc : new int[]{-1, 1}) {
            Position target = from.offset(dir, dc);
            if (!target.isValid()) continue;
            Piece captured = board.getPieceAt(target);
            if (captured != null && captured.getColor() != getColor()) {
                if (target.row == promoRow) {
                    addPromotionMoves(moves, from, target, captured);
                } else {
                    moves.add(new Move(from, target, this, captured));
                }
            }
        }
    }

    private void addEnPassantMoves(List<Move> moves, Position from, ChessBoard board, int dir) {
        Move lastMove = board.getLastMove();
        if (lastMove == null) return;
        if (lastMove.getMovingPiece().getType() != PieceType.PAWN) return;
        if (Math.abs(lastMove.getFrom().row - lastMove.getTo().row) != 2) return;

        // The opponent pawn that just moved must be adjacent horizontally
        Position opponentPawn = lastMove.getTo();
        if (opponentPawn.row != from.row) return;
        if (Math.abs(opponentPawn.col - from.col) != 1) return;

        // En passant capture square is diagonally ahead
        Position epTarget = from.offset(dir, opponentPawn.col - from.col);
        if (epTarget.isValid()) {
            Piece capturedPawn = board.getPieceAt(opponentPawn);
            moves.add(Move.enPassant(from, epTarget, this, capturedPawn, opponentPawn));
        }
    }

    private void addPromotionMoves(List<Move> moves, Position from, Position to, Piece captured) {
        for (PieceType promoType : PROMOTION_TYPES) {
            moves.add(Move.promotion(from, to, this, captured, promoType));
        }
    }
}
