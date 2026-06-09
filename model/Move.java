package chess.model;

import chess.model.pieces.Piece;

/**
 * Immutable value object representing a chess move.
 *
 * <p>Uses static factory methods instead of a sprawling constructor
 * to make each special move type explicit and readable.</p>
 */
public final class Move {

    public enum MoveType {
        NORMAL,
        CASTLE_KINGSIDE,
        CASTLE_QUEENSIDE,
        EN_PASSANT,
        PROMOTION
    }

    private final Position from;
    private final Position to;
    private final Piece movingPiece;
    private final Piece capturedPiece;
    private final MoveType moveType;
    private final PieceType promotionType;      // only for PROMOTION
    private final Position enPassantCapturePos; // only for EN_PASSANT

    // ─── Private constructor ─────────────────────────────────────────────────

    private Move(Position from, Position to, Piece movingPiece, Piece capturedPiece,
                 MoveType moveType, PieceType promotionType, Position enPassantCapturePos) {
        this.from = from;
        this.to = to;
        this.movingPiece = movingPiece;
        this.capturedPiece = capturedPiece;
        this.moveType = moveType;
        this.promotionType = promotionType;
        this.enPassantCapturePos = enPassantCapturePos;
    }

    // ─── Static factory methods ──────────────────────────────────────────────

    /** Normal move or capture. */
    public Move(Position from, Position to, Piece movingPiece, Piece capturedPiece) {
        this(from, to, movingPiece, capturedPiece, MoveType.NORMAL, null, null);
    }

    /** En passant capture. */
    public static Move enPassant(Position from, Position to, Piece movingPiece,
                                 Piece capturedPiece, Position capturePos) {
        return new Move(from, to, movingPiece, capturedPiece,
                MoveType.EN_PASSANT, null, capturePos);
    }

    /** Pawn promotion (with or without capture). */
    public static Move promotion(Position from, Position to, Piece movingPiece,
                                 Piece capturedPiece, PieceType promoType) {
        return new Move(from, to, movingPiece, capturedPiece,
                MoveType.PROMOTION, promoType, null);
    }

    /** Kingside castle (O-O). */
    public static Move kingsideCastle(Position from, Position to, Piece king) {
        return new Move(from, to, king, null, MoveType.CASTLE_KINGSIDE, null, null);
    }

    /** Queenside castle (O-O-O). */
    public static Move queensideCastle(Position from, Position to, Piece king) {
        return new Move(from, to, king, null, MoveType.CASTLE_QUEENSIDE, null, null);
    }

    // ─── Accessors ───────────────────────────────────────────────────────────

    public Position getFrom()           { return from; }
    public Position getTo()             { return to; }
    public Piece getMovingPiece()       { return movingPiece; }
    public Piece getCapturedPiece()     { return capturedPiece; }
    public MoveType getMoveType()       { return moveType; }
    public PieceType getPromotionType() { return promotionType; }
    public Position getEnPassantCapturePos() { return enPassantCapturePos; }

    public boolean isCapture()           { return capturedPiece != null; }
    public boolean isCastle()            { return moveType == MoveType.CASTLE_KINGSIDE
                                                || moveType == MoveType.CASTLE_QUEENSIDE; }
    public boolean isKingsideCastle()    { return moveType == MoveType.CASTLE_KINGSIDE; }
    public boolean isQueensideCastle()   { return moveType == MoveType.CASTLE_QUEENSIDE; }
    public boolean isEnPassant()         { return moveType == MoveType.EN_PASSANT; }
    public boolean isPromotion()         { return moveType == MoveType.PROMOTION; }

    // ─── Algebraic notation ──────────────────────────────────────────────────

    /** Short algebraic notation (SAN) representation. */
    @Override
    public String toString() {
        if (isKingsideCastle())  return "O-O";
        if (isQueensideCastle()) return "O-O-O";

        StringBuilder sb = new StringBuilder();
        PieceType type = movingPiece.getType();

        if (type != PieceType.PAWN) {
            sb.append(type.getAlgebraicLetter());
        }
        if (isCapture() || isEnPassant()) {
            if (type == PieceType.PAWN) sb.append(from.getFile());
            sb.append('x');
        }
        sb.append(to.toString());
        if (isPromotion()) {
            sb.append('=').append(promotionType.getAlgebraicLetter());
        }
        return sb.toString();
    }

    /** Long algebraic notation (e.g. "e2e4"). */
    public String toLongAlgebraic() {
        String base = from.toString() + to.toString();
        if (isPromotion()) base += promotionType.getAlgebraicLetter().toLowerCase();
        return base;
    }
}
