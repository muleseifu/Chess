package chess.model.pieces;

import chess.model.Move;
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;
import chess.model.board.ChessBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * The Rook piece.
 * Slides horizontally or vertically any number of squares.
 * Participates in castling (with the King).
 */
public class Rook extends Piece {

    private static final int[][] DIRECTIONS = {
        { 1,  0},
        {-1,  0},
        { 0,  1},
        { 0, -1}
    };

    public Rook(PieceColor color) {
        super(PieceType.ROOK, color);
    }

    private Rook(Rook other) {
        super(other);
    }

    @Override
    public Piece copy() {
        return new Rook(this);
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
