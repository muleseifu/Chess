package Chess;

import javax.swing.*;
import java.awt.*;
import Chess.gui.MainMenuPanel;
import Chess.model.game.Game;


/**
 * Main – application entry point.
 * Creates the JFrame and launches the main menu.
 */
public class Main {

    public static void main(String[] args) {
        // Run on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Use system look-and-feel if available
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            JFrame frame = new JFrame("Chess – ByteKnights");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            Game game = new Game();
            MainMenuPanel menu = new MainMenuPanel(frame, game);

            frame.setContentPane(menu);
            frame.pack();
            frame.setMinimumSize(new Dimension(700, 620));
            frame.setLocationRelativeTo(null); // centre on screen
            frame.setVisible(true);
        });
    }
}
