package chess.model.pieces;

import chess.model.Move;
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;
import chess.model.board.ChessBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * The Knight piece.
 * Moves in an L-shape: two squares in one direction and one square perpendicular.
 * The only piece that can jump over other pieces.
 */
public class Knight extends Piece {

    private static final int[][] DELTAS = {
        {-2, -1}, {-2, 1},
        {-1, -2}, {-1, 2},
        { 1, -2}, { 1, 2},
        { 2, -1}, { 2, 1}
    };

    public Knight(PieceColor color) {
        super(PieceType.KNIGHT, color);
    }

    private Knight(Knight other) {
        super(other);
    }

    @Override
    public Piece copy() {
        return new Knight(this);
    }

    @Override
    public List<Move> generatePseudoLegalMoves(Position from, ChessBoard board) {
        List<Move> moves = new ArrayList<>();
        for (int[] delta : DELTAS) {
            addStepMove(moves, from, from.offset(delta[0], delta[1]), board);
        }
        return moves;
    }
}
