package chess.model.state;

/**
 * Represents the current state of a chess game.
 */
public enum GameState {
    PLAYING,
    CHECK,
    CHECKMATE,
    STALEMATE,
    DRAW;

    public boolean isOver() {
        return this == CHECKMATE || this == STALEMATE || this == DRAW;
    }

    public String getDisplayMessage() {
        switch (this) {
            case CHECK:     return "Check!";
            case CHECKMATE: return "Checkmate!";
            case STALEMATE: return "Stalemate — Draw";
            case DRAW:      return "Draw";
            default:        return "";
        }
    }
}
