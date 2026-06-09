package chess.ui.renderer;

import chess.model.Move;
import chess.model.Position;

import java.awt.*;
import java.util.List;

/**
 * Draws a single board square and any applicable highlight layers.
 */
public class SquareRenderer {

    private SquareRenderer() {}

    public static void drawSquare(Graphics2D g2, int x, int y, int size,
                                  boolean isLight) {
        g2.setColor(isLight ? ColorTheme.LIGHT_SQUARE : ColorTheme.DARK_SQUARE);
        g2.fillRect(x, y, size, size);
    }

    public static void drawLastMoveHighlight(Graphics2D g2, int x, int y, int size) {
        g2.setColor(ColorTheme.LAST_MOVE);
        g2.fillRect(x, y, size, size);
    }

    public static void drawSelectedHighlight(Graphics2D g2, int x, int y, int size) {
        g2.setColor(ColorTheme.SELECTED);
        g2.fillRect(x, y, size, size);
    }

    public static void drawCheckHighlight(Graphics2D g2, int x, int y, int size) {
        g2.setColor(ColorTheme.CHECK_HIGHLIGHT);
        g2.fillRect(x, y, size, size);
    }

    /** Draw a small dot for a legal quiet move. */
    public static void drawLegalMoveDot(Graphics2D g2, int x, int y, int size) {
        int dotSize = size / 4;
        int ox = x + (size - dotSize) / 2;
        int oy = y + (size - dotSize) / 2;
        g2.setColor(ColorTheme.LEGAL_MOVE_DOT);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillOval(ox, oy, dotSize, dotSize);
    }

    /** Draw a ring for a legal capture move. */
    public static void drawLegalCaptureRing(Graphics2D g2, int x, int y, int size) {
        int thickness = size / 8;
        g2.setColor(ColorTheme.LEGAL_CAPTURE);
        g2.setStroke(new BasicStroke(thickness));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int pad = thickness / 2 + 2;
        g2.drawOval(x + pad, y + pad, size - pad * 2, size - pad * 2);
        g2.setStroke(new BasicStroke(1));
    }

    /** Draw rank/file label in the corner of a square. */
    public static void drawLabel(Graphics2D g2, String label, int x, int y,
                                 int size, boolean isLight) {
        g2.setFont(new Font("SansSerif", Font.BOLD, size / 6));
        g2.setColor(isLight ? ColorTheme.DARK_SQUARE : ColorTheme.LIGHT_SQUARE);
        g2.drawString(label, x + 3, y + size - 3);
    }
}
