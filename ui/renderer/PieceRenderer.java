package chess.ui.renderer;

import chess.model.pieces.Piece;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for drawing a single chess piece onto a Graphics2D context.
 * Uses large Unicode symbols rendered with a bold font.
 */
public class PieceRenderer {

    /** Cache fonts so we don't recreate them every paint cycle. */
    private static final Map<Integer, Font> FONT_CACHE = new HashMap<>();

    private PieceRenderer() {}

    /**
     * Draw {@code piece} centered inside the square whose top-left corner
     * is at ({@code x}, {@code y}) and whose side length is {@code squareSize}.
     */
    public static void draw(Graphics2D g2, Piece piece, int x, int y, int squareSize) {
        if (piece == null) return;

        String symbol = piece.getUnicodeSymbol();
        int    fontSize = (int) (squareSize * 0.72);
        Font   font     = FONT_CACHE.computeIfAbsent(fontSize,
                            s -> new Font("Segoe UI Symbol", Font.PLAIN, s));

        g2.setFont(font);
        FontMetrics fm  = g2.getFontMetrics();
        int sw = fm.stringWidth(symbol);
        int sh = fm.getAscent();

        int tx = x + (squareSize - sw) / 2;
        int ty = y + (squareSize + sh) / 2 - (int)(squareSize * 0.06);

        // Drop shadow
        g2.setColor(new java.awt.Color(0, 0, 0, 90));
        g2.drawString(symbol, tx + 2, ty + 2);

        // Piece
        g2.setColor(piece.isWhite()
            ? new java.awt.Color(255, 255, 255)
            : new java.awt.Color(30,  30,  30));
        g2.drawString(symbol, tx, ty);
    }
}
