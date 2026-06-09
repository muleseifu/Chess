package chess.model;

/**
 * Represents the color of a chess piece or player side.
 */
public enum PieceColor {
    WHITE, BLACK;

    public PieceColor opposite() {
        return this == WHITE ? BLACK : WHITE;
    }

    public String displayName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
