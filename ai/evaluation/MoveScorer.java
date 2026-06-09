package chess.ai.evaluation;

import chess.model.Move;
import chess.model.PieceType;

/**
 * Assigns a heuristic score to a move for move-ordering in alpha-beta search.
 * Better-scoring moves are searched first, which improves pruning efficiency.
 */
public class MoveScorer {

    private MoveScorer() {}

    /**
     * Higher score = search this move first.
     */
    public static int score(Move move) {
        int s = 0;

        // MVV-LVA: Most Valuable Victim, Least Valuable Attacker
        if (move.isCapture()) {
            s += move.getCapturedPiece().getMaterialValue()
               - move.getMovingPiece().getMaterialValue() / 10;
        }

        if (move.isCastle())                           s += 60;
        if (move.getPromotionType() == PieceType.QUEEN) s += 900;

        return s;
    }
}
