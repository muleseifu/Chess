package chess.ai.strategy;

import chess.ai.evaluation.MaterialAndPositionalEvaluator;

/**
 * Factory that creates the correct {@link AIStrategy} for a given difficulty level.
 */
public class AIStrategyFactory {

    private AIStrategyFactory() {}

    public enum Difficulty { EASY, MEDIUM, HARD }

    public static AIStrategy create(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:   return new RandomStrategy();
            case MEDIUM: return new MinimaxStrategy(3, new MaterialAndPositionalEvaluator());
            case HARD:   return new MinimaxStrategy(5, new MaterialAndPositionalEvaluator());
            default: throw new IllegalArgumentException("Unknown difficulty: " + difficulty);
        }
    }
}
