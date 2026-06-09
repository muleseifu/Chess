package chess.ui;

import chess.ui.MainMenuPanel.GameSettings;
import chess.ui.renderer.ColorTheme;

import javax.swing.*;
import java.awt.*;

public class MainMenuWindow extends JFrame {

    public MainMenuWindow() {
        super("Chess Main Menu");

        MainMenuPanel menuPanel = new MainMenuPanel();
        getContentPane().setBackground(ColorTheme.PANEL_BACKGROUND);
        setLayout(new BorderLayout());
        add(menuPanel, BorderLayout.CENTER);

        menuPanel.setOnPlay(settings -> {
            dispose();                          
            launchGame(settings);               
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
