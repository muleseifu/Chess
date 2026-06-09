package chess.model;

/**
 * Represents the type of a chess piece.
 * Each type has a standard material value used for evaluation.
 */
public enum PieceType {
    PAWN(100),
    KNIGHT(320),
    BISHOP(330),
    ROOK(500),
    QUEEN(900),
    KING(20000);

    private final int materialValue;

    PieceType(int materialValue) {
        this.materialValue = materialValue;
    }

    public int getMaterialValue() {
        return materialValue;
    }

    /** Returns the standard algebraic notation letter for this piece type. */
    public String getAlgebraicLetter() {
        switch (this) {
            case KING:   return "K";
            case QUEEN:  return "Q";
            case ROOK:   return "R";
            case BISHOP: return "B";
            case KNIGHT: return "N";
            case PAWN:   return "";
            default:     return "?";
        }
    }
}
