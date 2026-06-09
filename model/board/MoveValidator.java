package chess.model.board;

import chess.model.Move;
import chess.model.PieceColor;
import chess.model.Position;
import chess.model.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

/**
 * Filters pseudo-legal moves to fully legal moves by checking
 * that no move leaves the moving side's king in check.
 *
 * <p>This class is separate from {@code Board} to honour the
 * Single Responsibility Principle — the board stores state;
 * this class applies the legality filter.</p>
 */
public class MoveValidator {

    private MoveValidator() {}

    /**
     * From a list of pseudo-legal moves for a piece, return only those
     * that do not leave the moving side's king in check.
     *
     * @param pseudoLegal the candidate moves to filter
     * @param board       the current board (will not be mutated)
     * @return a new list containing only fully legal moves
     */
    public static List<Move> filterLegal(List<Move> pseudoLegal, Board board) {
        List<Move> legal = new ArrayList<>();
        for (Move m : pseudoLegal) {
            Board copy = new Board(board);
            copy.applyMoveInternal(m);
            PieceColor movingColor = m.getMovingPiece().getColor();
            if (!copy.isInCheck(movingColor)) {
                legal.add(m);
            }
        }
        return legal;
    }

    /**
     * Check whether any piece of {@code byColor} attacks the given square.
     */
    public static boolean isSquareAttacked(Position pos, PieceColor byColor, Board board) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPieceAt(new Position(r, c));
                if (p == null || p.getColor() != byColor) continue;
                List<Move> pseudo = p.generatePseudoLegalMoves(new Position(r, c), board);
                for (Move m : pseudo) {
                    if (m.getTo().equals(pos)) return true;
                }
            }
        }
        return false;
    }
}
