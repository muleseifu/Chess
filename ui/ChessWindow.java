package chess.ui;

import chess.core.GameController;
import chess.core.GameController.GameMode;
import chess.core.GameEventListener;
import chess.model.Move;
import chess.model.PieceColor;
import chess.model.Position;
import chess.model.board.Board;
import chess.model.state.GameState;
import chess.ui.dialogs.GameOverDialog;
import chess.ui.dialogs.GameOverDialog.Choice;
import chess.ui.dialogs.PromotionDialog;
import chess.ui.panels.BoardPanel;
import chess.ui.panels.ControlPanel;
import chess.ui.panels.ControlPanel.GameSettings;
import chess.ui.panels.StatusPanel;
import chess.ui.renderer.ColorTheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The main application window.
 *
 * <p>Wires the {@link GameController} to the UI panels and implements
 * {@link GameEventListener} to keep the view in sync with the model.</p>
 *
 * <p>All Swing updates are dispatched on the Event Dispatch Thread via
 * {@link SwingUtilities#invokeLater}.</p>
 */
public class ChessWindow extends JFrame implements GameEventListener {

    private final GameController controller;
    private final BoardPanel     boardPanel;
    private final StatusPanel    statusPanel;
    private final ControlPanel   controlPanel;
    private boolean boardFlipped = false;

    // Remembered so Restart can replay with identical settings
    private chess.ai.strategy.AIStrategyFactory.Difficulty lastDifficulty =
            chess.ai.strategy.AIStrategyFactory.Difficulty.MEDIUM;

    public ChessWindow() {
        super("Chess — OOP Edition");
        this.controller = new GameController();

        boardPanel   = new BoardPanel(controller);
        statusPanel  = new StatusPanel();
        controlPanel = new ControlPanel();

        controller.setEventListener(this);
        setupLayout();
        setupControls();
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // Kick off initial board display
        controller.startNewGame(GameMode.HUMAN_VS_HUMAN,
                chess.ai.strategy.AIStrategyFactory.Difficulty.MEDIUM,
                PieceColor.WHITE);
    }

    /**
     * Parameterised constructor — called by MainMenuWindow with the player's
     * chosen settings so the game begins immediately.
     */
    public ChessWindow(GameMode mode,
                       chess.ai.strategy.AIStrategyFactory.Difficulty difficulty,
                       PieceColor humanColor) {
        super("Chess — OOP Edition");
        this.controller = new GameController();
        this.lastDifficulty = difficulty;
        boardPanel   = new BoardPanel(controller);
        statusPanel  = new StatusPanel();
        controlPanel = new ControlPanel();
        controller.setEventListener(this);
        setupLayout();
        setupControls();
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        controller.startNewGame(mode, difficulty, humanColor);
        boardFlipped = (humanColor == PieceColor.BLACK);
        boardPanel.setFlipped(boardFlipped);
    }

    // ─── Layout ───────────────────────────────────────────────────────────────

    private void setupLayout() {
        getContentPane().setBackground(ColorTheme.PANEL_BACKGROUND);
        setLayout(new BorderLayout(0, 0));

        add(controlPanel, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(ColorTheme.PANEL_BACKGROUND);
        center.add(boardPanel,  BorderLayout.CENTER);
        center.add(statusPanel, BorderLayout.EAST);
        add(center, BorderLayout.CENTER);
    }

    private void setupControls() {
        controlPanel.setOnNewGame(this::handleNewGame);
        controlPanel.setOnFlip(() -> {
            boardFlipped = !boardFlipped;
            boardPanel.setFlipped(boardFlipped);
        });
    }

    private void handleNewGame(GameSettings settings) {
        lastDifficulty = settings.difficulty;
        controller.startNewGame(settings.mode, settings.difficulty, settings.humanColor);
        boardFlipped = settings.humanColor == PieceColor.BLACK;
        boardPanel.setFlipped(boardFlipped);
    }

    // ─── GameEventListener ───────────────────────────────────────────────────

    @Override
    public void onBoardChanged(Board board) {
        SwingUtilities.invokeLater(() -> {
            boardPanel.updateBoard(board);
            statusPanel.update(board);
        });
    }

    @Override
    public void onSelectionChanged(Position selected, List<Move> legalMoves) {
        SwingUtilities.invokeLater(() ->
            boardPanel.updateSelection(selected, legalMoves));
    }

    @Override
    public void onGameOver(GameState finalState, PieceColor losingColor) {
        SwingUtilities.invokeLater(() -> {
            Choice choice = GameOverDialog.show(this, finalState, losingColor);
            switch (choice) {
                case RESTART:
                    // Replay with the exact same mode, difficulty, and color
                    controller.startNewGame(controller.getGameMode(),
                            lastDifficulty,
                            controller.getHumanColor());
                    boardFlipped = (controller.getHumanColor() == PieceColor.BLACK);
                    boardPanel.setFlipped(boardFlipped);
                    break;
                case MAIN_MENU:
                    controller.shutdown();
                    dispose();
                    SwingUtilities.invokeLater(() -> {
                        chess.ui.MainMenuWindow menu = new chess.ui.MainMenuWindow();
                        menu.setVisible(true);
                    });
                    break;
                case QUIT:
                default:
                    System.exit(0);
            }
        });
    }

    @Override
    public void onAIThinking(boolean thinking) {
        SwingUtilities.invokeLater(() -> {
            statusPanel.setAIThinking(thinking);
            boardPanel.setEnabled(!thinking);
        });
    }
}

/* ── second constructor added for MainMenuWindow ── */
