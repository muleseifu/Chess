package Chess.gui;

import Chess.model.game.Game;
import Chess.util.Constants.Difficulty;
import Chess.util.Constants.GameMode;
import Chess.ai.*;
import javax.swing.*;
import java.awt.*;

/**
 * MainMenuPanel – first screen shown on application launch.
 * Handles game mode selection (PvP / PvB) and settings access.
 */
public class MainMenuPanel extends JPanel {

    private JButton   btnPvP;
    private JButton   btnPvB;
    private JButton   btnSettings;
    private JLabel    titleLabel;
    private JComboBox<String> difficultyBox;

    /** Reference to the main application frame for panel switching. */
    private JFrame parentFrame;
    private Game   game;

    public MainMenuPanel(JFrame parentFrame, Game game) {
        this.parentFrame = parentFrame;
        this.game = game;
        initUI();
    }

    // -------------------------------------------------------------------------
    // UI Construction
    // -------------------------------------------------------------------------

    /** Build and lay out all UI components. */
    public void initUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.gridx  = 0;
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // Title label
        titleLabel = new JLabel("♟ Chess", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0; gbc.insets = new Insets(30, 20, 20, 20);
        add(titleLabel, gbc);

        // PvP button
        btnPvP = createMenuButton("Play vs Person");
        btnPvP.addActionListener(e -> onPvPClicked());
        gbc.gridy = 1; gbc.insets = new Insets(10, 60, 5, 60);
        add(btnPvP, gbc);

        // PvB button
        btnPvB = createMenuButton("Play vs Bot");
        btnPvB.addActionListener(e -> onPvBClicked());
        gbc.gridy = 2;
        add(btnPvB, gbc);

        // Difficulty selector (initially hidden)
        difficultyBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        difficultyBox.setSelectedIndex(1); // default Medium
        difficultyBox.setVisible(false);
        difficultyBox.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 3;
        add(difficultyBox, gbc);

        // Settings button
        btnSettings = createMenuButton("⚙ Settings");
        btnSettings.setFont(new Font("Arial", Font.PLAIN, 14));
        btnSettings.addActionListener(e -> onSettingsClicked());
        gbc.gridy = 4; gbc.insets = new Insets(20, 80, 10, 80);
        add(btnSettings, gbc);

        // Show difficulty selector when PvB is hovered/selected
        btnPvB.addActionListener(e -> setDifficultyVisible(true));
        btnPvP.addActionListener(e -> setDifficultyVisible(false));
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        return btn;
    }

    // -------------------------------------------------------------------------
    // Actions
    // -------------------------------------------------------------------------

    /** Start Game in PVP mode; switch to GamePanel. */
    public void onPvPClicked() {
        game.startGame(GameMode.PVP);
        switchToGamePanel();
    }

    /** Read difficulty; start Game in PVB mode. */
    public void onPvBClicked() {
        setDifficultyVisible(true);
        String sel = (String) difficultyBox.getSelectedItem();

        Difficulty diff;
        switch (sel) {
            case "Easy":  diff = Difficulty.EASY;   break;
            case "Hard":  diff = Difficulty.HARD;   break;
            default:      diff = Difficulty.MEDIUM;
        }
        game.startGame(GameMode.PVB, diff);
        switchToGamePanel();
    }

    /** Open SettingsDialog. */
    public void onSettingsClicked() {
        SettingsDialog dialog = new SettingsDialog(parentFrame);
        dialog.setVisible(true);
    }

    /** Show/hide difficulty selector. */
    public void setDifficultyVisible(boolean b) {
        difficultyBox.setVisible(b);
        revalidate();
        repaint();
    }

    private void switchToGamePanel() {
        GamePanel gamePanel = new GamePanel(parentFrame, game);
        parentFrame.setContentPane(gamePanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
