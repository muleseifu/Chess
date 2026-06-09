package chess.ai.evaluation;

import chess.model.board.Board;

/**
 * Strategy interface for board evaluation.
 * Implementations return a score from White's perspective:
 * positive = White advantage, negative = Black advantage.
 */
public interface BoardEvaluator {
    int evaluate(Board board);
}
