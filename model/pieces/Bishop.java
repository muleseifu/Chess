package chess.model.pieces;

import chess.model.Move;
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;
import chess.model.board.ChessBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * The Bishop piece.
 * Slides diagonally any number of squares.
 * Each bishop is confined to squares of a single color throughout the game.
 */
public class Bishop extends Piece {

    private static final int[][] DIRECTIONS = {
        { 1,  1},
        { 1, -1},
        {-1,  1},
        {-1, -1}
    };

    public Bishop(PieceColor color) {
        super(PieceType.BISHOP, color);
    }

    private Bishop(Bishop other) {
        super(other);
    }

    @Override
    public Piece copy() {
        return new Bishop(this);
    }

    @Override
    public List<Move> generatePseudoLegalMoves(Position from, ChessBoard board) {
        List<Move> moves = new ArrayList<>();
        for (int[] dir : DIRECTIONS) {
            addSlidingMoves(moves, from, board, dir);
        }
        return moves;
    }
}
