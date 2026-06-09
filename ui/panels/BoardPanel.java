package chess.ui.panels;

import chess.core.GameController;
import chess.model.Move;
import chess.model.Position;
import chess.model.board.Board;
import chess.ui.renderer.BoardRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * The Swing panel that displays the chess board.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Delegate all painting to {@link BoardRenderer} (no rendering logic here).</li>
 *   <li>Translate mouse clicks into board positions and forward them to
 *       {@link GameController}.</li>
 *   <li>Store a snapshot of the model state needed for the next repaint.</li>
 * </ul>
 * </p>
 */
public class BoardPanel extends JPanel {

    private static final int DEFAULT_SQUARE_SIZE = 80;

    private final GameController controller;
    private final BoardRenderer  renderer;

    private Board      board;
    private Position   selected;
    private List<Move> legalMoves;
    private boolean    flipped = false;

    public BoardPanel(GameController controller) {
        this.controller = controller;
        this.renderer   = new BoardRenderer(DEFAULT_SQUARE_SIZE);

        int boardPx = DEFAULT_SQUARE_SIZE * 8;
        setPreferredSize(new Dimension(boardPx, boardPx));
        setMinimumSize(new Dimension(boardPx, boardPx));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
    }

    // ─── Public update API (called by ChessWindow on events) ─────────────────

    public void updateBoard(Board board) {
        this.board = board;
        repaint();
    }

    public void updateSelection(Position selected, List<Move> legalMoves) {
        this.selected   = selected;
        this.legalMoves = legalMoves;
        repaint();
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
        repaint();
    }

    // ─── Painting ─────────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (board == null) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderer.paint(g2, board, selected, legalMoves, flipped);
    }

    // ─── Click handling ───────────────────────────────────────────────────────

    private void handleClick(int pixelX, int pixelY) {
        int col = pixelX / DEFAULT_SQUARE_SIZE;
        int row = pixelY / DEFAULT_SQUARE_SIZE;
        if (flipped) { col = 7 - col; row = 7 - row; }

        Position pos = new Position(row, col);
        if (pos.isValid()) controller.onSquareClicked(pos);
    }
}
