package chess.ui;

import chess.ui.MainMenuPanel.GameSettings;
import chess.ui.renderer.ColorTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Standalone window that shows the main menu before the game begins.
 *
 * <p>When the player clicks <em>Play</em> this window disposes itself
 * and hands the chosen {@link GameSettings} to {@link ChessWindow} via
 * a simple callback — zero coupling to the rest of the model.</p>
 *
 * <p>To add this feature to the project you only need to:
 * <ol>
 *   <li>Add this file.</li>
 *   <li>Add {@link MainMenuPanel}.</li>
 *   <li>Change two lines in {@code Main.java} (show this window instead of
 *       {@code ChessWindow} directly).</li>
 * </ol>
 * No existing class is modified.</p>
 */
public class MainMenuWindow extends JFrame {

    public MainMenuWindow() {
        super("Chess Main Menu");

        MainMenuPanel menuPanel = new MainMenuPanel();
        getContentPane().setBackground(ColorTheme.PANEL_BACKGROUND);
        setLayout(new BorderLayout());
        add(menuPanel, BorderLayout.CENTER);

        // When the player hits Play: close menu → open game
        menuPanel.setOnPlay(settings -> {
            dispose();                          // close menu window
            launchGame(settings);               // open game window
        });

        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setMinimumSize(new Dimension(600, 800));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setLocationRelativeTo(null);            // center on screen
    }

    private void launchGame(GameSettings settings) {
        SwingUtilities.invokeLater(() -> {
            ChessWindow game = new ChessWindow(
                    settings.mode,
                    settings.difficulty,
                    settings.humanColor
            );
            game.setVisible(true);
        });
    }
}
