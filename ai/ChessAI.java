package chess.ai;

import chess.ai.strategy.AIStrategy;
import chess.ai.strategy.AIStrategyFactory;
import chess.ai.strategy.AIStrategyFactory.Difficulty;
import chess.model.Move;
import chess.model.PieceColor;
import chess.model.board.Board;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Facade for AI operations.
 *
 * <p>Runs the chosen strategy on a background thread so the UI stays responsive.
 * Callers supply a {@code Consumer<Move>} callback that is invoked when the AI
 * has finished thinking.</p>
 */
public class ChessAI {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private       AIStrategy      strategy;
    private       Difficulty      difficulty;

    public ChessAI(Difficulty difficulty) {
        setDifficulty(difficulty);
    }

    /** Change difficulty mid-game. */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.strategy   = AIStrategyFactory.create(difficulty);
    }

    public Difficulty getDifficulty() { return difficulty; }

    /**
     * Ask the AI to pick a move asynchronously.
     *
     * @param board    a snapshot of the current board (caller may pass a copy)
     * @param color    the side the AI is playing
     * @param callback invoked on the calling (UI) thread concept with the chosen move
     */
    public void computeBestMoveAsync(Board board, PieceColor color, Consumer<Move> callback) {
        executor.submit(() -> {
            Move move = null;
            try {
                move = strategy.chooseBestMove(new Board(board), color);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            final Move selectedMove = move;
            javax.swing.SwingUtilities.invokeLater(() -> callback.accept(selectedMove));
        });
    }

    /** Synchronous version (useful for testing). */
    public Move computeBestMove(Board board, PieceColor color) {
        return strategy.chooseBestMove(new Board(board), color);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
