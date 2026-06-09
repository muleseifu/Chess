package chess.core;

import chess.ai.ChessAI;
import chess.ai.strategy.AIStrategyFactory.Difficulty;
import chess.model.Move;
import chess.model.PieceColor;
import chess.model.Position;
import chess.model.board.Board;
import chess.model.state.GameState;

import java.util.List;

/**
 * Application-layer controller.
 *
 * <p>Sits between the UI and the model. The UI calls methods here;
 * the controller updates the board and triggers AI moves when needed.
 * It knows nothing about Swing — that dependency is reversed through
 * the {@link GameEventListener} interface.</p>
 *
 * <p>OOP patterns used:
 * <ul>
 *   <li><b>Observer</b> – {@link GameEventListener} decouples UI from model.</li>
 *   <li><b>Facade</b>   – single entry point for all game actions.</li>
 * </ul>
 * </p>
 */
public class GameController {

    public enum GameMode { HUMAN_VS_HUMAN, HUMAN_VS_AI }

    private Board             board;
    private final ChessAI     ai;
    private GameMode          gameMode;
    private PieceColor        humanColor;   // relevant in HUMAN_VS_AI mode

    private Position          selectedSquare;
    private List<Move>        highlightedMoves;

    private GameEventListener eventListener;

    // ─── Construction ────────────────────────────────────────────────────────

    public GameController() {
        this.board     = new Board();
        this.ai        = new ChessAI(Difficulty.MEDIUM);
        this.gameMode  = GameMode.HUMAN_VS_HUMAN;
        this.humanColor = PieceColor.WHITE;
    }

    // ─── Game lifecycle ──────────────────────────────────────────────────────

    public void startNewGame(GameMode mode, Difficulty difficulty, PieceColor humanColor) {
        this.board      = new Board();
        this.gameMode   = mode;
        this.humanColor = humanColor;
        this.ai.setDifficulty(difficulty);
        this.selectedSquare  = null;
        this.highlightedMoves = null;

        notifyBoardChanged();

        // If human plays black, AI goes first
        if (mode == GameMode.HUMAN_VS_AI && humanColor == PieceColor.BLACK) {
            triggerAIMove();
        }
    }

    // ─── Square selection and move handling ──────────────────────────────────

    /**
     * Called when the player clicks on a board square.
     */
    public void onSquareClicked(Position pos) {
        if (board.getGameState().isOver()) return;
        if (gameMode == GameMode.HUMAN_VS_AI && board.getCurrentTurn() != humanColor) return;

        if (selectedSquare == null) {
            trySelectPiece(pos);
        } else {
            if (pos.equals(selectedSquare)) {
                clearSelection();
            } else if (isHighlightedMove(pos)) {
                executePlayerMove(pos);
            } else {
                clearSelection();
                trySelectPiece(pos);
            }
        }

        notifySelectionChanged();
    }

    private void trySelectPiece(Position pos) {
        var piece = board.getPieceAt(pos);
        if (piece == null || piece.getColor() != board.getCurrentTurn()) return;
        selectedSquare   = pos;
        highlightedMoves = board.getLegalMovesForPiece(pos);
    }

    private void executePlayerMove(Position to) {
        Move moveRequest = buildMoveRequest(to);
        if (moveRequest == null) { clearSelection(); return; }

        boolean applied = board.makeMove(moveRequest);
        clearSelection();

        if (applied) {
            notifyBoardChanged();
            checkGameOver();
            if (!board.getGameState().isOver() && gameMode == GameMode.HUMAN_VS_AI) {
                triggerAIMove();
            }
        }
    }

    private Move buildMoveRequest(Position to) {
        if (highlightedMoves == null) return null;
        for (Move m : highlightedMoves) {
            if (m.getTo().equals(to)) {
                // For promotions, always default to Queen (UI can override via promoteSelection)
                return m;
            }
        }
        return null;
    }

    /** Called by UI promotion dialog to pick a specific promotion type. */
    public void executePromotionMove(Move move) {
        board.makeMove(move);
        clearSelection();
        notifyBoardChanged();
        checkGameOver();
        if (!board.getGameState().isOver() && gameMode == GameMode.HUMAN_VS_AI) {
            triggerAIMove();
        }
    }

    // ─── AI move ─────────────────────────────────────────────────────────────

    private void triggerAIMove() {
        PieceColor aiColor = humanColor.opposite();
        if (eventListener != null) eventListener.onAIThinking(true);

        ai.computeBestMoveAsync(board, aiColor, move -> {
            if (eventListener != null) eventListener.onAIThinking(false);
            if (move != null) {
                board.makeMove(move);
                notifyBoardChanged();
                checkGameOver();
            }
        });
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private boolean isHighlightedMove(Position pos) {
        if (highlightedMoves == null) return false;
        return highlightedMoves.stream().anyMatch(m -> m.getTo().equals(pos));
    }

    private void clearSelection() {
        selectedSquare   = null;
        highlightedMoves = null;
    }

    private void checkGameOver() {
        GameState state = board.getGameState();
        if (state.isOver() && eventListener != null) {
            // After a move, currentTurn has already flipped to the NEXT player.
            // In CHECKMATE that next player is the one in checkmate — the loser.
            // In STALEMATE it is also the stalemated (side with no moves) player.
            eventListener.onGameOver(state, board.getCurrentTurn());
        }
    }

    private void notifyBoardChanged() {
        if (eventListener != null) eventListener.onBoardChanged(board);
    }

    private void notifySelectionChanged() {
        if (eventListener != null)
            eventListener.onSelectionChanged(selectedSquare, highlightedMoves);
    }

    // ─── Accessors ───────────────────────────────────────────────────────────

    public Board              getBoard()            { return board; }
    public Position           getSelectedSquare()   { return selectedSquare; }
    public List<Move>         getHighlightedMoves() { return highlightedMoves; }
    public GameMode           getGameMode()         { return gameMode; }
    public PieceColor         getHumanColor()       { return humanColor; }

    public void setEventListener(GameEventListener listener) {
        this.eventListener = listener;
    }

    public void shutdown() { ai.shutdown(); }
}
