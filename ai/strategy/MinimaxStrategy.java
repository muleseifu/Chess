package chess.ai.strategy;

import chess.ai.evaluation.BoardEvaluator;
import chess.ai.evaluation.MoveScorer;
import chess.model.Move;
import chess.model.PieceColor;
import chess.model.board.Board;
import chess.model.state.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimax with alpha-beta pruning.
 * Search depth and evaluator are injected, making this class reusable
 * for both Medium (depth 3) and Hard (depth 5) difficulty levels.
 */
public class MinimaxStrategy implements AIStrategy {

    private final int            searchDepth;
    private final BoardEvaluator evaluator;

    public MinimaxStrategy(int searchDepth, BoardEvaluator evaluator) {
        this.searchDepth = searchDepth;
        this.evaluator   = evaluator;
    }

    @Override
    public Move chooseBestMove(Board board, PieceColor color) {
        List<Move> moves = sortedMoves(board.getLegalMoves(color));
        if (moves.isEmpty()) return null;

        Move bestMove  = null;
        int  bestScore = Integer.MIN_VALUE;
        int  alpha     = Integer.MIN_VALUE;
        int  beta      = Integer.MAX_VALUE;
        boolean maximizing = (color == PieceColor.WHITE);

        for (Move move : moves) {
            Board copy = new Board(board);
            copy.applyMoveInternal(move);
            int score = maximizing
                ? minimax(copy, searchDepth - 1, alpha, beta, false)
                : -minimax(copy, searchDepth - 1, alpha, beta, true);

            if (score > bestScore) {
                bestScore = score;
                bestMove  = move;
            }
            alpha = Math.max(alpha, score);
        }
        return bestMove;
    }

    private int minimax(Board board, int depth, int alpha, int beta, boolean maximizing) {
        GameState state = board.getGameState();

        if (depth == 0 || state.isOver()) {
            if (state == GameState.CHECKMATE) return maximizing ? -30000 - depth : 30000 + depth;
            if (state == GameState.STALEMATE) return 0;
            return evaluator.evaluate(board);
        }

        PieceColor color = board.getCurrentTurn();
        List<Move> moves = sortedMoves(board.getLegalMoves(color));

        if (maximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : moves) {
                Board copy = new Board(board);
                copy.applyMoveInternal(move);
                int eval = minimax(copy, depth - 1, alpha, beta, false);
                maxEval  = Math.max(maxEval, eval);
                alpha    = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : moves) {
                Board copy = new Board(board);
                copy.applyMoveInternal(move);
                int eval = minimax(copy, depth - 1, alpha, beta, true);
                minEval  = Math.min(minEval, eval);
                beta     = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }

    private List<Move> sortedMoves(List<Move> moves) {
        List<Move> sorted = new ArrayList<>(moves);
        sorted.sort((a, b) -> MoveScorer.score(b) - MoveScorer.score(a));
        return sorted;
    }
}
