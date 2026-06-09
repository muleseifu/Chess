package chess.core;

import chess.model.Move;
import chess.model.PieceColor;
import chess.model.Position;
import chess.model.board.Board;
import chess.model.state.GameState;

import java.util.List;

/**
 * Observer interface for game events.
 *
 * <p>The UI implements this to receive model notifications without
 * the model knowing anything about Swing.</p>
 */
public interface GameEventListener {

    /** Called after any move is applied and board state changes. */
    void onBoardChanged(Board board);

    /** Called when a piece is selected or deselected. */
    void onSelectionChanged(Position selected, List<Move> legalMoves);

    /** Called when the game ends. */
    void onGameOver(GameState finalState, PieceColor losingColor);

    /** Called when the AI starts or finishes thinking. */
    void onAIThinking(boolean thinking);
}
