package chess.ui;

import chess.ai.strategy.AIStrategyFactory.Difficulty;
import chess.core.GameController.GameMode;
import chess.model.PieceColor;
import chess.ui.renderer.ColorTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

/**
 * The main menu panel shown before a game starts.
 *
 * <p>Completely isolated from the rest of the codebase — it only
 * fires a {@link GameSettings} record to whoever is listening.
 * Nothing here knows about Board, Piece, or ChessWindow.</p>
 */
public class MainMenuPanel extends JPanel {

    /** Carries the player's choices back to the launcher. */
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

    private Consumer<GameSettings> onPlay;

    // ── widgets ──────────────────────────────────────────────────────────────
    private final JComboBox<String> modeCombo;
    private final JComboBox<String> diffCombo;
    private final JComboBox<String> colorCombo;

    public MainMenuPanel() {
        setBackground(ColorTheme.PANEL_BACKGROUND);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets    = new Insets(8, 8, 8, 8);
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx     = 0;

        // ── Title ─────────────────────────────────────────────────────────────
        JLabel title = new JLabel("          Play Chess       ", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Symbol", Font.BOLD, 52));
        title.setForeground(ColorTheme.PANEL_BACKGROUND);
        gbc.gridy = 0; gbc.insets = new Insets(0, 8, 24, 8);
        add(title, gbc);


        // ── Game mode ─────────────────────────────────────────────────────────
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.gridy  = 1;
        add(sectionLabel("Game Mode"), gbc);

        modeCombo = styledCombo("Human vs Human", "Human vs AI");
        modeCombo.setForeground(ColorTheme.PANEL_BACKGROUND);
        gbc.gridy = 2; add(modeCombo, gbc);

        // ── Difficulty ────────────────────────────────────────────────────────
        gbc.gridy = 3; add(sectionLabel("AI Difficulty"), gbc);

        diffCombo = styledCombo("Easy", "Medium", "Hard");
        diffCombo.setSelectedIndex(1);
        diffCombo.setForeground(ColorTheme.PANEL_BACKGROUND);
        gbc.gridy = 4; add(diffCombo, gbc);

        // ── Color ─────────────────────────────────────────────────────────────
        gbc.gridy = 5; add(sectionLabel("Play As"), gbc);

        colorCombo = styledCombo("White", "Black");
        gbc.gridy  = 6; add(colorCombo, gbc);

        // toggle difficulty visibility based on mode
        diffCombo.setEnabled(false); // default: HvH
        modeCombo.addActionListener(e ->
                diffCombo.setEnabled(modeCombo.getSelectedIndex() == 1));
        colorCombo.setEnabled(false);
        modeCombo.addActionListener(e ->
                colorCombo.setEnabled(modeCombo.getSelectedIndex() == 1));

        // ── Play button ───────────────────────────────────────────────────────
        JButton playBtn = playButton();
        playBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        playBtn.setForeground(ColorTheme.PANEL_BACKGROUND);

        gbc.gridy  = 7;
        gbc.insets = new Insets(28, 8, 0, 8);
        add(playBtn, gbc);
    }

    /** Register the callback that receives settings when the player clicks Play. */
    public void setOnPlay(Consumer<GameSettings> handler) { this.onPlay = handler; }

    // ── helpers ──────────────────────────────────────────────────────────────

    private GameSettings buildSettings() {
        GameMode mode = modeCombo.getSelectedIndex() == 0
                ? GameMode.HUMAN_VS_HUMAN : GameMode.HUMAN_VS_AI;

        Difficulty diff;
        switch (diffCombo.getSelectedIndex()) {
            case 0:  diff = Difficulty.EASY;  break;
            case 2:  diff = Difficulty.HARD;  break;
            default: diff = Difficulty.MEDIUM;
        }

        PieceColor color = colorCombo.getSelectedIndex() == 0
                ? PieceColor.WHITE : PieceColor.BLACK;

        return new GameSettings(mode, diff, color);
    }

    private JButton playButton() {
        JButton btn = new JButton("Play");
        btn.setFont(new Font("SansSerif", Font.BOLD, 18));
        btn.setBackground(new Color(34, 139, 34));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 48));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));

        // hover effect
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(50, 168, 50));
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(34, 139, 34));
            }
        });

        btn.addActionListener(e -> { if (onPlay != null) onPlay.accept(buildSettings()); });
        return btn;
    }

    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(new Color(180, 180, 180));
        return lbl;
    }

    private JComboBox<String> styledCombo(String... items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(ColorTheme.BUTTON_BACKGROUND);
        cb.setForeground(Color.WHITE);
        cb.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cb.setPreferredSize(new Dimension(220, 34));
        return cb;
    }
}
