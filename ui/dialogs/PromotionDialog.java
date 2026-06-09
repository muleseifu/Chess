package chess.ui.dialogs;

import chess.model.PieceColor;
import chess.model.PieceType;
import chess.ui.renderer.ColorTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Modal dialog shown when a pawn reaches the back rank.
 * Returns the {@link PieceType} the player wants to promote to.
 */
public class PromotionDialog extends JDialog {

    private PieceType chosen = PieceType.QUEEN; // default

    private static final PieceType[] OPTIONS = {
        PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT
    };

    private static final String[][] SYMBOLS = {
        {"\u2655", "\u265B"},  // queen
        {"\u2656", "\u265C"},  // rook
        {"\u2657", "\u265D"},  // bishop
        {"\u2658", "\u265E"}   // knight
    };

    public PromotionDialog(Frame owner, PieceColor color) {
        super(owner, "Choose Promotion", true);
        setBackground(ColorTheme.PANEL_BACKGROUND);
        setLayout(new FlowLayout());
        getContentPane().setBackground(ColorTheme.PANEL_BACKGROUND);

        Font symbolFont = new Font("Segoe UI Symbol", Font.PLAIN, 52);
        int  colorIdx   = color == PieceColor.WHITE ? 0 : 1;

        for (int i = 0; i < OPTIONS.length; i++) {
            final PieceType type = OPTIONS[i];
            JButton btn = new JButton(SYMBOLS[i][colorIdx]);
            btn.setFont(symbolFont);
            btn.setBackground(ColorTheme.BUTTON_BACKGROUND);
            btn.setForeground(ColorTheme.BUTTON_TEXT);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(72, 80));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> { chosen = type; dispose(); });
            add(btn);
        }

        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    /** Show dialog and return the chosen type. */
    public static PieceType show(Frame owner, PieceColor color) {
        PromotionDialog dlg = new PromotionDialog(owner, color);
        dlg.setVisible(true);
        return dlg.chosen;
    }
}
