package chess.ai.strategy;

import chess.model.Move;
import chess.model.PieceColor;
import chess.model.board.Board;

/**
 * Strategy interface for AI move selection.
 * Different difficulty levels use different implementations.
 */
public interface AIStrategy {
    /**
     * Choose the best move for {@code color} on the given board.
     * Returns null if no moves are available.
     */
    Move chooseBestMove(Board board, PieceColor color);
}
