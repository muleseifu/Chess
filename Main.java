package chess;

import chess.ui.MainMenuWindow;

import javax.swing.*;

/**
 * Application entry point.
 *
 * <p>Launches the Swing UI on the Event Dispatch Thread as required
 * by Swing's single-thread model.</p>
 *
 * <h2>How to compile and run</h2>
 * <pre>
 *   # From the project root (where /src lives):
 *   javac -d out $(find src -name "*.java")
 *   java -cp out chess.Main
 * </pre>
 */
public class Main {

    public static void main(String[] args) {
        // Use system look-and-feel so the window decorations match the OS
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            MainMenuWindow menu = new MainMenuWindow();
            menu.setVisible(true);
        });
    }
}
