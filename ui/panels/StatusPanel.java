package chess.ui.panels;

import chess.model.PieceColor;
import chess.model.board.Board;
import chess.model.state.GameState;
import chess.ui.renderer.ColorTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Displays whose turn it is, the current game state, and move history.
 */
public class StatusPanel extends JPanel {

    private final JLabel  statusLabel;
    private final JLabel  turnLabel;
    private final JLabel  thinkingLabel;
    private final JTextArea moveHistoryArea;

    public StatusPanel() {
        setBackground(ColorTheme.PANEL_BACKGROUND);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        setPreferredSize(new Dimension(220, 640));

        turnLabel     = createLabel("", 16, Font.BOLD);
        statusLabel   = createLabel("", 14, Font.PLAIN);
        thinkingLabel = createLabel("AI thinking…", 13, Font.ITALIC);
        thinkingLabel.setForeground(ColorTheme.THINKING_COLOR);
        thinkingLabel.setVisible(false);

        JLabel histTitle = createLabel("Move History", 13, Font.BOLD);
        histTitle.setBorder(BorderFactory.createEmptyBorder(16, 0, 4, 0));

        moveHistoryArea = new JTextArea();
        moveHistoryArea.setEditable(false);
        moveHistoryArea.setBackground(new Color(55, 55, 60));
        moveHistoryArea.setForeground(ColorTheme.STATUS_TEXT);
        moveHistoryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(moveHistoryArea);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(turnLabel);
        add(Box.createVerticalStrut(6));
        add(statusLabel);
        add(thinkingLabel);
        add(histTitle);
        add(Box.createVerticalStrut(4));
        add(scroll);
    }

    public void update(Board board) {
        PieceColor turn  = board.getCurrentTurn();
        GameState  state = board.getGameState();

        turnLabel.setText((turn == PieceColor.WHITE ? "⬜ White" : "⬛ Black") + "'s turn");
        statusLabel.setText(state.getDisplayMessage());
        statusLabel.setForeground(state == GameState.CHECK || state == GameState.CHECKMATE
            ? new Color(220, 50, 50)
            : ColorTheme.STATUS_TEXT);

        // Rebuild move history
        StringBuilder sb = new StringBuilder();
        var history = board.getMoveHistory();
        for (int i = 0; i < history.size(); i++) {
            if (i % 2 == 0) sb.append((i / 2 + 1)).append(". ");
            sb.append(history.get(i).toString()).append("  ");
            if (i % 2 != 0) sb.append("\n");
        }
        moveHistoryArea.setText(sb.toString());
    }

    public void setAIThinking(boolean thinking) {
        thinkingLabel.setVisible(thinking);
        repaint();
    }

    private JLabel createLabel(String text, int size, int style) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", style, size));
        lbl.setForeground(ColorTheme.STATUS_TEXT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
}
