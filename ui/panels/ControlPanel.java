package chess.ui.panels;

import chess.ai.strategy.AIStrategyFactory.Difficulty;
import chess.core.GameController.GameMode;
import chess.model.PieceColor;
import chess.ui.renderer.ColorTheme;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Toolbar panel with game controls: new game, flip, difficulty.
 */
public class ControlPanel extends JPanel {

    private final JComboBox<String>     modeCombo;
    private final JComboBox<String>     difficultyCombo;
    private final JComboBox<String>     colorCombo;
    private final JButton               newGameBtn;
    private final JButton               flipBtn;

    private Consumer<GameSettings> onNewGame;
    private Runnable               onFlip;

    public ControlPanel() {
        setBackground(ColorTheme.PANEL_BACKGROUND);
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 6));

        modeCombo       = styledCombo("Human vs Human", "Human vs AI");
        difficultyCombo = styledCombo("Easy", "Medium", "Hard");
        colorCombo      = styledCombo("Play as White", "Play as Black");
        newGameBtn      = styledButton("New Game");
        flipBtn         = styledButton("Flip Board");

        difficultyCombo.setSelectedIndex(1);

        add(labeled("Mode: "));
        add(modeCombo);
        add(labeled("AI: "));
        add(difficultyCombo);
        add(labeled("Color: "));
        add(colorCombo);
        add(newGameBtn);
        add(flipBtn);

        modeCombo.addActionListener(e ->
            difficultyCombo.setEnabled(modeCombo.getSelectedIndex() == 1));

        newGameBtn.addActionListener(e -> {
            if (onNewGame != null) onNewGame.accept(buildSettings());
        });

        flipBtn.addActionListener(e -> {
            if (onFlip != null) onFlip.run();
        });
    }

    private GameSettings buildSettings() {
        GameMode mode = modeCombo.getSelectedIndex() == 0
            ? GameMode.HUMAN_VS_HUMAN : GameMode.HUMAN_VS_AI;
        Difficulty diff;
        switch (difficultyCombo.getSelectedIndex()) {
            case 0:  diff = Difficulty.EASY;   break;
            case 2:  diff = Difficulty.HARD;   break;
            default: diff = Difficulty.MEDIUM;
        }
        PieceColor color = colorCombo.getSelectedIndex() == 0
            ? PieceColor.WHITE : PieceColor.BLACK;
        return new GameSettings(mode, diff, color);
    }

    public void setOnNewGame(Consumer<GameSettings> handler) { this.onNewGame = handler; }
    public void setOnFlip(Runnable handler)                   { this.onFlip   = handler; }

    // ─── Factories ────────────────────────────────────────────────────────────

    private JComboBox<String> styledCombo(String... items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(ColorTheme.BUTTON_BACKGROUND);
        cb.setForeground(ColorTheme.BUTTON_TEXT);
        cb.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return cb;
    }

    private JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(ColorTheme.BUTTON_BACKGROUND);
        btn.setForeground(ColorTheme.BUTTON_TEXT);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel labeled(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(ColorTheme.STATUS_TEXT);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return lbl;
    }

    // ─── Inner settings DTO ──────────────────────────────────────────────────

    public static class GameSettings {
        public final GameMode   mode;
        public final Difficulty difficulty;
        public final PieceColor humanColor;

        public GameSettings(GameMode mode, Difficulty difficulty, PieceColor humanColor) {
            this.mode       = mode;
            this.difficulty = difficulty;
            this.humanColor = humanColor;
        }
    }
}
