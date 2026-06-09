package chess.ai.strategy;

import chess.model.Move;
import chess.model.PieceColor;
import chess.model.board.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Easy AI strategy: prefers captures 70% of the time, otherwise plays randomly.
 */
public class RandomStrategy implements AIStrategy {

    private final Random random = new Random();

    @Override
    public Move chooseBestMove(Board board, PieceColor color) {
        List<Move> moves = board.getLegalMoves(color);
        if (moves.isEmpty()) return null;

        List<Move> captures = new ArrayList<>();
        for (Move m : moves) {
            if (m.isCapture() || m.isEnPassant()) captures.add(m);
        }

        if (!captures.isEmpty() && random.nextDouble() < 0.70) {
            return captures.get(random.nextInt(captures.size()));
        }
        return moves.get(random.nextInt(moves.size()));
    }
}
