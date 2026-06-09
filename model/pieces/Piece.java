package chess.model.pieces;

import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;
import chess.model.board.ChessBoard;

import java.util.List;

/**
 * Abstract base class for all chess pieces.
 *
 * <p>Uses the Template Method pattern: subclasses implement
 * {@link #generatePseudoLegalMoves(Position, ChessBoard)} to define
 * their specific movement rules, while this class handles common
 * state (color, type, hasMoved) and provides shared utilities.</p>
 */
public abstract class Piece {

    private PieceType type;
    private final PieceColor color;
    private boolean hasMoved;

    protected Piece(PieceType type, PieceColor color) {
        this.type = type;
        this.color = color;
        this.hasMoved = false;
    }

    /** Copy constructor. */
    protected Piece(Piece other) {
        this.type = other.type;
        this.color = other.color;
        this.hasMoved = other.hasMoved;
    }

    // ─── Template method ────────────────────────────────────────────────────

    /**
     * Generate all pseudo-legal moves for this piece from the given position.
     * Pseudo-legal means moves that follow the piece's movement rules,
     * but do NOT filter out moves that leave the king in check.
     *
     * @param from  the current position of this piece
     * @param board the board state to generate moves from
     * @return list of pseudo-legal moves
     */
    public abstract List<chess.model.Move> generatePseudoLegalMoves(Position from, ChessBoard board);

    /**
     * Create a deep copy of this piece (used for board copying).
     */
    public abstract Piece copy();

    // ─── Shared helpers for subclasses ──────────────────────────────────────

    /**
     * Slide in a direction until hitting a piece or the board edge.
     * Adds quiet moves and, at most, one capture.
     */
    protected void addSlidingMoves(List<chess.model.Move> moves, Position from,
                                   ChessBoard board, int[] direction) {
        Position pos = from.offset(direction[0], direction[1]);
        while (pos.isValid()) {
            Piece target = board.getPieceAt(pos);
            if (target == null) {
                moves.add(new chess.model.Move(from, pos, this, null));
            } else {
                if (target.getColor() != this.color) {
                    moves.add(new chess.model.Move(from, pos, this, target));
                }
                break; // blocked
            }
            pos = pos.offset(direction[0], direction[1]);
        }
    }

    /**
     * Add a single-step move (for kings, knights) if the square is empty or capturable.
     */
    protected void addStepMove(List<chess.model.Move> moves, Position from,
                               Position to, ChessBoard board) {
        if (!to.isValid()) return;
        Piece target = board.getPieceAt(to);
        if (target == null || target.getColor() != this.color) {
            moves.add(new chess.model.Move(from, to, this, target));
        }
    }

    // ─── Getters / Setters ───────────────────────────────────────────────────

    public PieceType getType()   { return type; }
    public PieceColor getColor() { return color; }
    public boolean hasMoved()    { return hasMoved; }

    public void setHasMoved(boolean hasMoved) { this.hasMoved = hasMoved; }
    public void setType(PieceType type)       { this.type = type; }

    public int getMaterialValue() { return type.getMaterialValue(); }

    public boolean isWhite() { return color == PieceColor.WHITE; }
    public boolean isBlack() { return color == PieceColor.BLACK; }

    public boolean isEnemy(Piece other) {
        return other != null && other.getColor() != this.color;
    }

    // ─── Unicode symbols ─────────────────────────────────────────────────────

    /** Returns the Unicode chess symbol for this piece. */
    public String getUnicodeSymbol() {
        if (color == PieceColor.WHITE) {
            switch (type) {
                case KING:   return "\u2654";
                case QUEEN:  return "\u2655";
                case ROOK:   return "\u2656";
                case BISHOP: return "\u2657";
                case KNIGHT: return "\u2658";
                case PAWN:   return "\u2659";
            }
        } else {
            switch (type) {
                case KING:   return "\u265A";
                case QUEEN:  return "\u265B";
                case ROOK:   return "\u265C";
                case BISHOP: return "\u265D";
                case KNIGHT: return "\u265E";
                case PAWN:   return "\u265F";
            }
        }
        return "?";
    }

    @Override
    public String toString() {
        return color + "_" + type;
    }
}
