package chess.model;

/**
 * Immutable value object representing a board position (row, col).
 * Row 0 = rank 8 (black's back rank), Row 7 = rank 1 (white's back rank).
 * Col 0 = file 'a', Col 7 = file 'h'.
 */
public final class Position {

    public final int row;
    public final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean isValid() {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public Position offset(int dRow, int dCol) {
        return new Position(row + dRow, col + dCol);
    }

    /** Returns the algebraic file letter ('a'-'h'). */
    public char getFile() {
        return (char) ('a' + col);
    }

    /** Returns the algebraic rank number (1-8). */
    public int getRank() {
        return 8 - row;
    }

    @Override
    public String toString() {
        return "" + getFile() + getRank();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return row == other.row && col == other.col;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    }
}
