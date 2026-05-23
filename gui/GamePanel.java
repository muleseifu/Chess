package Chess.gui;

import Chess.model.game.Game;
import Chess.model.pieces.Piece;
import Chess.util.Constants.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * GamePanel – main game screen.
 * Contains the board, captured pieces, move history, and controls.
 * Implements BoardListener to refresh whenever board state changes.
 */
public class GamePanel extends JPanel implements BoardListener {

    private BoardPanel   boardPanel;
    private SidePanel    sidePanel;
    private ControlPanel controlPanel;
    private JLabel       statusLabel;
    private JLabel       turnIndicator;
    private Game         game;
    private JFrame       parentFrame;

    public GamePanel(JFrame parentFrame, Game game) {
        this.parentFrame = parentFrame;
        this.game = game;
        initUI();
        // Register as board listener so we refresh on every move
        game.getBoard().addListener(this);
    }

    // -------------------------------------------------------------------------
    // UI Construction
    // -------------------------------------------------------------------------

    public void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(30, 30, 30));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Board (centre)
        boardPanel = new BoardPanel(game);
        add(boardPanel, BorderLayout.CENTER);

        // Side panel (right): captured pieces + move history
        sidePanel = new SidePanel();
        add(sidePanel, BorderLayout.EAST);

        // Control panel (bottom): resign / draw / undo / menu
        controlPanel = new ControlPanel(parentFrame, game, this);
        add(controlPanel, BorderLayout.SOUTH);

        // Status bar (top)
        JPanel topBar = buildTopBar();
        add(topBar, BorderLayout.NORTH);
    }

    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(50, 50, 50));
        top.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        turnIndicator = new JLabel("White's Turn", SwingConstants.LEFT);
        turnIndicator.setForeground(Color.WHITE);
        turnIndicator.setFont(new Font("Arial", Font.BOLD, 16));

        statusLabel = new JLabel("", SwingConstants.RIGHT);
        statusLabel.setForeground(new Color(255, 200, 0));
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));

        top.add(turnIndicator, BorderLayout.WEST);
        top.add(statusLabel,   BorderLayout.EAST);
        return top;
    }

    // -------------------------------------------------------------------------
    // BoardListener callback
    // -------------------------------------------------------------------------

    @Override
    public void onBoardChanged() {
        boardPanel.refreshAll();
        updateStatusLabel();
        updateTurnIndicator();

        // Update captured pieces and history
        sidePanel.updateCapturedPieces(getCapturedPieces(0)); // white captured
        sidePanel.updateCapturedPieces(getCapturedPieces(1)); // black captured
        sidePanel.updateMoveHistory(game.getHistory().getMoveList());

        // Show game-over dialog if needed
        GameStatus status = game.getStatus();
        if (status == GameStatus.CHECKMATE || status == GameStatus.STALEMATE
                || status == GameStatus.DRAW || status == GameStatus.RESIGNED) {
            showGameOverDialog(buildResultMessage(status));
        }
    }

    // -------------------------------------------------------------------------
    // Dialogs
    // -------------------------------------------------------------------------

    /** Modal result dialog with Play Again / Menu options. */
    public void showGameOverDialog(String msg) {
        SwingUtilities.invokeLater(() -> {
            int choice = JOptionPane.showOptionDialog(
                this, msg, "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Play Again", "Main Menu"},
                "Play Again"
            );
            if (choice == 0) {
                game.restartGame();
                boardPanel.refreshAll();
                updateStatusLabel();
                updateTurnIndicator();
            } else {
                returnToMenu();
            }
        });
    }

    /** Let player pick promotion piece — returns "Q", "R", "B", or "N". */
    public String showPromotionDialog() {
        String[] options = {"Queen", "Rook", "Bishop", "Knight"};
        int choice = JOptionPane.showOptionDialog(
            this, "Choose promotion piece:", "Pawn Promotion",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]
        );
        switch (choice) {
            case 1: return "R";
            case 2: return "B";
            case 3: return "N";
            default: return "Q";
        }
    }

    // -------------------------------------------------------------------------
    // Label updates
    // -------------------------------------------------------------------------

    public void updateStatusLabel() {
        GameStatus status = game.getStatus();
        switch (status) {
            case CHECK:     statusLabel.setText("CHECK!");        break;
            case CHECKMATE: statusLabel.setText("CHECKMATE!");    break;
            case STALEMATE: statusLabel.setText("STALEMATE!");    break;
            case DRAW:      statusLabel.setText("DRAW!");         break;
            case RESIGNED:  statusLabel.setText("RESIGNED!");     break;
            default:        statusLabel.setText("");
        }
    }

    public void updateTurnIndicator() {
        int turn = game.getCurrentTurn();
        String name = (turn == 0) ? "White" : "Black";
        turnIndicator.setText(name + "'s Turn");
        turnIndicator.setForeground(turn == 0 ? Color.WHITE : new Color(180, 180, 180));
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private List<Piece> getCapturedPieces(int color) {
        // Pieces of 'color' that are no longer available
        java.util.List<Piece> all = (color == 0)
            ? game.getBoard().getWhitePieces()
            : game.getBoard().getBlackPieces();
        java.util.List<Piece> captured = new java.util.ArrayList<>();
        for (Piece p : all) {
            if (!p.isAvailable()) captured.add(p);
        }
        return captured;
    }

    private String buildResultMessage(GameStatus status) {
        switch (status) {
            case CHECKMATE:
                String winner = (game.getCurrentTurn() == 0) ? "Black" : "White";
                return winner + " wins by checkmate!";
            case STALEMATE: return "Draw by stalemate.";
            case DRAW:      return "Draw agreed.";
            case RESIGNED:
                String res = (game.getCurrentTurn() == 0) ? "Black" : "White";
                return res + " wins by resignation.";
            default:        return "Game over.";
        }
    }

    private void returnToMenu() {
        MainMenuPanel menu = new MainMenuPanel(parentFrame, game);
        parentFrame.setContentPane(menu);
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    // Expose for ControlPanel
    public Game getGame() { return game; }
}
