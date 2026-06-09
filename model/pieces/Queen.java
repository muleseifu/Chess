package chess.model.pieces;

import chess.model.Move;
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;
import chess.model.board.ChessBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * The Queen piece.
 * Combines the movement of a Rook and a Bishop:
 * slides horizontally, vertically, or diagonally any number of squares.
 * The most powerful piece on the board.
 */
public class Queen extends Piece {

    /** All eight sliding directions (rook + bishop combined). */
    private static final int[][] DIRECTIONS = {
        { 1,  0}, {-1,  0}, { 0,  1}, { 0, -1},  // rook directions
        { 1,  1}, { 1, -1}, {-1,  1}, {-1, -1}   // bishop directions
    };

    public Queen(PieceColor color) {
        super(PieceType.QUEEN, color);
    }

    private Queen(Queen other) {
        super(other);
    }

    @Override
    public Piece copy() {
        return new Queen(this);
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
