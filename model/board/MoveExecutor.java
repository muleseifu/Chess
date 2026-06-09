package chess.model.board;

import chess.model.Move;
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;
import chess.model.pieces.Piece;
import chess.model.pieces.PieceFactory;

/**
 * Responsible for physically applying a {@link Move} to the board squares array.
 * Handles all special cases: castling rook movement, en passant capture removal,
 * pawn promotion, and castling-rights revocation.
 *
 * <p>Separating move execution from board state keeps {@code Board} focused
 * on coordination, not on the mechanical details of each move type.</p>
 */
public class MoveExecutor {

    private MoveExecutor() {}

    /**
     * Apply {@code move} to {@code squares}, updating castling rights as needed.
     *
     * @param squares the 8×8 board array (modified in place)
     * @param move    the move to apply
     * @param rights  castling rights to update
     */
    public static void apply(Piece[][] squares, Move move, CastlingRights rights) {
        Position from = move.getFrom();
        Position to   = move.getTo();
        Piece piece   = squares[from.row][from.col];

        // Move piece to target square
        squares[to.row][to.col]     = piece;
        squares[from.row][from.col] = null;
        if (piece != null) piece.setHasMoved(true);

        switch (move.getMoveType()) {
            case EN_PASSANT:
                applyEnPassant(squares, move);
                break;
            case CASTLE_KINGSIDE:
                applyCastleKingside(squares, to.row);
                break;
            case CASTLE_QUEENSIDE:
                applyCastleQueenside(squares, to.row);
                break;
            case PROMOTION:
                applyPromotion(squares, move, to);
                break;
            default:
                break;
        }

        updateCastlingRights(move, piece, from, rights);
    }

    private static void applyEnPassant(Piece[][] squares, Move move) {
        Position capPos = move.getEnPassantCapturePos();
        squares[capPos.row][capPos.col] = null;
    }

    private static void applyCastleKingside(Piece[][] squares, int row) {
        // Move rook from h-file (col 7) to f-file (col 5)
        Piece rook = squares[row][7];
        squares[row][5] = rook;
        squares[row][7] = null;
        if (rook != null) rook.setHasMoved(true);
    }

    private static void applyCastleQueenside(Piece[][] squares, int row) {
        // Move rook from a-file (col 0) to d-file (col 3)
        Piece rook = squares[row][0];
        squares[row][3] = rook;
        squares[row][0] = null;
        if (rook != null) rook.setHasMoved(true);
    }

    private static void applyPromotion(Piece[][] squares, Move move, Position to) {
        PieceType promoType = move.getPromotionType();
        PieceColor color    = move.getMovingPiece().getColor();
        Piece promoted      = PieceFactory.create(promoType, color);
        promoted.setHasMoved(true);
        squares[to.row][to.col] = promoted;
    }

    private static void updateCastlingRights(Move move, Piece piece,
                                             Position from, CastlingRights rights) {
        if (piece == null) return;

        // King move revokes both sides for that color
        if (piece.getType() == PieceType.KING) {
            rights.revokeBoth(piece.getColor());
        }

        // Rook move revokes the appropriate side
        if (piece.getType() == PieceType.ROOK) {
            if (from.row == 7) {
                if (from.col == 7) rights.revokeKingside(PieceColor.WHITE);
                if (from.col == 0) rights.revokeQueenside(PieceColor.WHITE);
            }
            if (from.row == 0) {
                if (from.col == 7) rights.revokeKingside(PieceColor.BLACK);
                if (from.col == 0) rights.revokeQueenside(PieceColor.BLACK);
            }
        }
    }
}
