package Chess.gui;

import Chess.model.game.Game;
import Chess.util.Constants.*;
import javax.swing.*;
import java.awt.*;

/**
 * ControlPanel – action buttons: Resign, Offer Draw, Undo, Restart, Main Menu.
 */
public class ControlPanel extends JPanel {

    private JFrame    parentFrame;
    private Game      game;
    private GamePanel gamePanel;

    public ControlPanel(JFrame parentFrame, Game game, GamePanel gamePanel) {
        this.parentFrame = parentFrame;
        this.game        = game;
        this.gamePanel   = gamePanel;
        initUI();
    }

    private void initUI() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 12, 8));
        setBackground(new Color(40, 40, 40));

        add(createBtn("⚑ Resign",  new Color(180, 60,  60),  e -> onResignClicked()));
        add(createBtn("= Draw",    new Color(100, 100, 180), e -> onDrawClicked()));
        add(createBtn("↩ Undo",    new Color(80, 130, 80),   e -> onUndoClicked()));
        add(createBtn("↺ Restart", new Color(100, 100, 100), e -> onRestartClicked()));
        add(createBtn("⌂ Menu",    new Color(60,  60, 100),  e -> onMenuClicked()));
    }

    private JButton createBtn(String text, Color bg,
                               java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(listener);
        return btn;
    }

    // -------------------------------------------------------------------------
    // Button handlers
    // -------------------------------------------------------------------------

    public void onResignClicked() {
        int confirm = JOptionPane.showConfirmDialog(
            this, "Are you sure you want to resign?", "Resign",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            game.resignGame(game.getCurrentTurn());
            gamePanel.onBoardChanged();
        }
    }

    public void onDrawClicked() {
        int confirm = JOptionPane.showConfirmDialog(
            this, "Offer draw to opponent?", "Draw Offer",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // In PVP both players are at the same machine — auto-accept prompt
            int accept = JOptionPane.showConfirmDialog(
                this, "Opponent: Accept draw?", "Draw Offer",
                JOptionPane.YES_NO_OPTION);
            if (accept == JOptionPane.YES_OPTION) {
                game.acceptDraw();
                gamePanel.onBoardChanged();
            }
        }
    }

    public void onUndoClicked() {
        if (game.getGameMode() == GameMode.PVB) {
            JOptionPane.showMessageDialog(this, "Undo is not available in Bot mode.");
            return;
        }
        game.undoMove();
        gamePanel.onBoardChanged();
    }

    public void onMenuClicked() {
        int confirm = JOptionPane.showConfirmDialog(
            this, "Return to main menu? Current game will be lost.", "Main Menu",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Game newGame = new Game();
            MainMenuPanel menu = new MainMenuPanel(parentFrame, newGame);
            parentFrame.setContentPane(menu);
            parentFrame.revalidate();
            parentFrame.repaint();
        }
    }

    public void onRestartClicked() {
        int confirm = JOptionPane.showConfirmDialog(
            this, "Restart current game?", "Restart",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            game.restartGame();
            gamePanel.onBoardChanged();
        }
    }
}
