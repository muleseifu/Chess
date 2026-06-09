package chess.ui.dialogs;

import chess.model.PieceColor;
import chess.model.state.GameState;
import chess.ui.renderer.ColorTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Shows a game-over message with options to restart, return to the
 * main menu, or quit.
 */
public class GameOverDialog extends JDialog {

    public enum Choice { RESTART, MAIN_MENU, QUIT }

    private Choice choice = Choice.QUIT;

    public GameOverDialog(Frame owner, GameState state, PieceColor losingColor) {
        super(owner, "Game Over", true);
        getContentPane().setBackground(ColorTheme.PANEL_BACKGROUND);
        setLayout(new BorderLayout(12, 12));

        String msg = buildMessage(state, losingColor);
        JLabel label = new JLabel(msg, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setForeground(ColorTheme.STATUS_TEXT);
        label.setBorder(BorderFactory.createEmptyBorder(20, 24, 10, 24));

        JButton restartBtn   = styledButton("Restart");
        JButton mainMenuBtn  = styledButton("Main Menu");
        JButton quitBtn      = styledButton("Quit");
        
        restartBtn.setBackground(Color.WHITE);
        restartBtn.setForeground(Color.BLACK);
        
        mainMenuBtn.setBackground(Color.WHITE);
        mainMenuBtn.setForeground(Color.BLACK);
        
        quitBtn.setBackground(Color.WHITE);
        quitBtn.setForeground(Color.BLACK);

        restartBtn.addActionListener(e  -> { choice = Choice.RESTART;    dispose(); });
        mainMenuBtn.addActionListener(e -> { choice = Choice.MAIN_MENU;  dispose(); });
        quitBtn.addActionListener(e     -> { choice = Choice.QUIT;       dispose(); });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        buttons.setBackground(ColorTheme.PANEL_BACKGROUND);
        buttons.add(restartBtn);
        buttons.add(mainMenuBtn);
        buttons.add(quitBtn);
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        add(label,   BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(360, 160));
        setLocationRelativeTo(owner);
    }

    private String buildMessage(GameState state, PieceColor losingColor) {
        switch (state) {
            case CHECKMATE:
                return losingColor.opposite().displayName() + " wins by Checkmate!";
            case STALEMATE:
                return "Stalemate — it's a Draw!";
            case DRAW:
                return "Draw!";
            default:
                return "Game Over";
        }
    }

    public static Choice show(Frame owner, GameState state, PieceColor losingColor) {
        GameOverDialog dlg = new GameOverDialog(owner, state, losingColor);
        dlg.setVisible(true);
        return dlg.choice;
    }

    private JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.RED);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}