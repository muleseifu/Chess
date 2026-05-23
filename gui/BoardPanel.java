package Chess.gui;

import Chess.model.board.Board;
import Chess.model.board.Cell;
import Chess.model.game.Game;
import Chess.model.pieces.Piece;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * BoardPanel – renders the 8×8 grid and handles all user click interactions.
 */
public class BoardPanel extends JPanel {

    private static final int CELL_SIZE = 70;

    private JButton[][]  cellButtons = new JButton[8][8];
    private Cell         selectedCell = null;  // currently selected cell (null if none)
    private Game         game;

    public BoardPanel(Game game) {
        this.game = game;
        setLayout(new BorderLayout());
        initBoard();
    }

    // -------------------------------------------------------------------------
    // Initialisation
    // -------------------------------------------------------------------------

    /** Create JButtons for every cell and attach mouse listeners. */
    public void initBoard() {
        JPanel grid = new JPanel(new GridLayout(8, 8));
        grid.setPreferredSize(new Dimension(CELL_SIZE * 8, CELL_SIZE * 8));

        Board board = game.getBoard();

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                btn.setOpaque(true);
                btn.setBorderPainted(false);
                btn.setFocusPainted(false);

                Cell cell = board.getCell(r, c);
                btn.setBackground(cell.getBaseColor());

                final int row = r, col = c;
                btn.addActionListener(e -> onCellClicked(row, col));

                cellButtons[r][c] = btn;
                grid.add(btn);
            }
        }

        // Wrap with coordinate labels
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(addCoordinateLabels(grid), BorderLayout.CENTER);
        add(wrapper, BorderLayout.CENTER);

        refreshAll();
    }

    // -------------------------------------------------------------------------
    // Click handling
    // -------------------------------------------------------------------------

    /** Delegate click to game logic. */
    public void onCellClicked(int x, int y) {
        Cell cell = game.getBoard().getCell(x, y);
        game.handleCellClick(cell);
        refreshAll();
    }

    // -------------------------------------------------------------------------
    // Rendering
    // -------------------------------------------------------------------------

    /** Repaint all 64 cells from board state. */
    public void refreshAll() {
        Board board = game.getBoard();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                refreshCell(board.getCell(r, c));
            }
        }
    }

    /** Update a single cell's icon and border/background. */
    public void refreshCell(Cell cell) {
        JButton btn = cellButtons[cell.getX()][cell.getY()];

        // Background colour
        if (cell.isCheck()) {
            btn.setBackground(new Color(220, 50, 50));
        } else if (cell.isSelected()) {
            btn.setBackground(new Color(100, 200, 100));
        } else if (cell.isPossibleDestination()) {
            btn.setBackground(new Color(100, 149, 237, 200));
        } else {
            btn.setBackground(cell.getBaseColor());
        }

        // Piece sprite
        Piece piece = cell.getPiece();
        if (piece != null && piece.getPath() != null) {
            try {
                ImageIcon icon = new ImageIcon(piece.getPath());
                Image scaled  = icon.getImage().getScaledInstance(
                    CELL_SIZE - 10, CELL_SIZE - 10, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(scaled));
            } catch (Exception ex) {
                btn.setIcon(null);
                btn.setText(piece.getId());
                btn.setForeground(Color.WHITE);
            }
        } else {
            btn.setIcon(null);
            btn.setText("");
        }

        // Possible destination indicator (small dot overlay)
        if (cell.isPossibleDestination() && cell.isEmpty()) {
            btn.setText("●");
            btn.setForeground(new Color(50, 50, 200, 180));
            btn.setFont(new Font("Dialog", Font.PLAIN, 20));
        }
    }

    /** Show move-target indicators for a list of legal destination cells. */
    public void highlightLegalMoves(List<Cell> cells) {
        for (Cell cell : cells) {
            cell.setPossibleDestination();
            refreshCell(cell);
        }
    }

    /** Remove all move-target indicators. */
    public void clearHighlights() {
        game.getBoard().clearHighlights();
        refreshAll();
    }

    // -------------------------------------------------------------------------
    // Coordinate labels
    // -------------------------------------------------------------------------

    /** Draw A–H column labels and 1–8 row labels around the board. */
    private JPanel addCoordinateLabels(JPanel boardGrid) {
        JPanel outer = new JPanel(new BorderLayout(2, 2));
        outer.setBackground(new Color(40, 40, 40));

        // Column labels (A-H) — bottom
        JPanel colLabels = new JPanel(new GridLayout(1, 8));
        colLabels.setBackground(new Color(40, 40, 40));
        for (int c = 0; c < 8; c++) {
            JLabel lbl = new JLabel(String.valueOf((char) ('A' + c)), SwingConstants.CENTER);
            lbl.setForeground(Color.LIGHT_GRAY);
            lbl.setPreferredSize(new Dimension(CELL_SIZE, 20));
            colLabels.add(lbl);
        }

        // Row labels (8-1) — left
        JPanel rowLabels = new JPanel(new GridLayout(8, 1));
        rowLabels.setBackground(new Color(40, 40, 40));
        for (int r = 0; r < 8; r++) {
            JLabel lbl = new JLabel(String.valueOf(8 - r), SwingConstants.CENTER);
            lbl.setForeground(Color.LIGHT_GRAY);
            lbl.setPreferredSize(new Dimension(20, CELL_SIZE));
            rowLabels.add(lbl);
        }

        outer.add(rowLabels, BorderLayout.WEST);
        outer.add(boardGrid, BorderLayout.CENTER);
        outer.add(colLabels,  BorderLayout.SOUTH);
        return outer;
    }
}
