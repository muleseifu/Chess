package chess.ai.evaluation;

import chess.model.board.Board;

/**
 * Evaluates the board using material count and piece-square table bonuses.
 */
public class MaterialAndPositionalEvaluator implements BoardEvaluator {

    @Override
    public int evaluate(Board board) {
        return board.evaluate();
    }
}
