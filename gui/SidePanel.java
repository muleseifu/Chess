package Chess.gui;

import Chess.model.pieces.Piece;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * SidePanel – displays captured pieces, move history, and optional game clock.
 */
public class SidePanel extends JPanel {

    private JPanel     whiteCapturedPanel;
    private JPanel     blackCapturedPanel;
    private JTextArea  historyArea;
    private JLabel     whiteClockLabel;
    private JLabel     blackClockLabel;

    public SidePanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(50, 50, 50));
        setPreferredSize(new Dimension(200, 560));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- Clocks ----
        blackClockLabel = buildClockLabel("Black  ⏱ --:--");
        add(blackClockLabel);
        add(Box.createVerticalStrut(5));

        // ---- Black captured pieces ----
        add(sectionLabel("Black's captures:"));
        blackCapturedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        blackCapturedPanel.setBackground(new Color(60, 60, 60));
        blackCapturedPanel.setMaximumSize(new Dimension(180, 80));
        add(blackCapturedPanel);
        add(Box.createVerticalStrut(10));

        // ---- Move history ----
        add(sectionLabel("Move History:"));
        historyArea = new JTextArea(15, 14);
        historyArea.setEditable(false);
        historyArea.setBackground(new Color(30, 30, 30));
        historyArea.setForeground(Color.WHITE);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(historyArea);
        scroll.setMaximumSize(new Dimension(180, 300));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        add(scroll);
        add(Box.createVerticalStrut(10));

        // ---- White captured pieces ----
        add(sectionLabel("White's captures:"));
        whiteCapturedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        whiteCapturedPanel.setBackground(new Color(60, 60, 60));
        whiteCapturedPanel.setMaximumSize(new Dimension(180, 80));
        add(whiteCapturedPanel);
        add(Box.createVerticalStrut(5));

        // ---- White clock ----
        whiteClockLabel = buildClockLabel("White  ⏱ --:--");
        add(whiteClockLabel);
    }

    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(new Color(180, 180, 180));
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    private JLabel buildClockLabel(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Monospaced", Font.BOLD, 16));
        lbl.setBackground(new Color(70, 70, 70));
        lbl.setOpaque(true);
        lbl.setMaximumSize(new Dimension(180, 35));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    // -------------------------------------------------------------------------
    // Public update methods
    // -------------------------------------------------------------------------

    /** Refresh captured-piece display for one side. */
    public void updateCapturedPieces(List<Piece> pieces) {
        if (pieces.isEmpty()) return;
        int color = pieces.get(0).getColor();
        JPanel panel = (color == 0) ? whiteCapturedPanel : blackCapturedPanel;
        panel.removeAll();

        for (Piece p : pieces) {
            JLabel lbl = new JLabel();
            if (p.getPath() != null) {
                try {
                    ImageIcon icon = new ImageIcon(p.getPath());
                    lbl.setIcon(new ImageIcon(
                        icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH)));
                } catch (Exception ex) {
                    lbl.setText(p.getId());
                    lbl.setForeground(Color.WHITE);
                }
            } else {
                lbl.setText(p.getId());
                lbl.setForeground(Color.WHITE);
            }
            panel.add(lbl);
        }
        panel.revalidate();
        panel.repaint();
    }

    /** Append move list in algebraic notation. */
    public void updateMoveHistory(List<String> moves) {
        historyArea.setText("");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < moves.size(); i += 2) {
            int moveNum = (i / 2) + 1;
            String white = moves.get(i);
            String black = (i + 1 < moves.size()) ? moves.get(i + 1) : "";
            sb.append(String.format("%-3d %-8s %s%n", moveNum, white, black));
        }
        historyArea.setText(sb.toString());
        // Scroll to bottom
        historyArea.setCaretPosition(historyArea.getDocument().getLength());
    }

    /** Refresh countdown timers (seconds remaining). */
    public void updateClock(int whiteSeconds, int blackSeconds) {
        whiteClockLabel.setText("White  ⏱ " + formatTime(whiteSeconds));
        blackClockLabel.setText("Black  ⏱ " + formatTime(blackSeconds));
    }

    private String formatTime(int totalSeconds) {
        int mins = totalSeconds / 60;
        int secs = totalSeconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }
}
