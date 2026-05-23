package Chess.model.game;

import Chess.ai.Bot;
import Chess.model.board.Board;
import Chess.model.board.Cell;
import Chess.model.pieces.Piece;
import Chess.util.Constants.*;
import Chess.util.MoveHistory;
import Chess.util.MoveRecord;

import static Chess.util.Constants.WHITE;
import static Chess.util.Constants.BLACK;

/**
 * Game – orchestrates turn management, win/draw detection, and mode selection.
 * Central controller linking the board, GUI, and AI.
 */
public class Game {

    private Board      board;
    private int        currentTurn; // 0 = White, 1 = Black
    private GameMode   gameMode;
    private Bot        bot;         // null in PVP mode
    private MoveHistory history;
    private GameStatus status;

    // Currently selected cell (for two-click move selection)
    private Cell selectedCell = null;

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    /**
     * Initialise board, pieces, and reset turn.
     */
    public void startGame(GameMode mode) {
        this.gameMode   = mode;
        this.board      = new Board();
        this.currentTurn = WHITE;
        this.history    = new MoveHistory();
        this.status     = GameStatus.ONGOING;
        this.selectedCell = null;

        if (mode == GameMode.PVB) {
            // Bot always plays Black by default
            this.bot = new Bot(BLACK, Difficulty.MEDIUM);
        } else {
            this.bot = null;
        }
    }

    public void startGame(GameMode mode, Difficulty difficulty) {
        startGame(mode);
        if (bot != null) bot.setDifficulty(difficulty);
    }

    // -------------------------------------------------------------------------
    // Click handling
    // -------------------------------------------------------------------------

    /**
     * Main click dispatcher: select / deselect / move based on click.
     */
    public void handleCellClick(Cell cell) {
        if (status != GameStatus.ONGOING && status != GameStatus.CHECK) return;

        if (selectedCell == null) {
            // Nothing selected — try to select own piece
            if (!cell.isEmpty() && cell.getPiece().getColor() == currentTurn) {
                selectCell(cell);
            }
        } else {
            if (cell.isPossibleDestination()) {
                // Execute move
                makeMove(selectedCell, cell);
                clearSelection();
            } else if (!cell.isEmpty() && cell.getPiece().getColor() == currentTurn) {
                // Switch selection to another own piece
                clearSelection();
                selectCell(cell);
            } else {
                // Clicked elsewhere — deselect
                clearSelection();
            }
        }
    }

    private void selectCell(Cell cell) {
        selectedCell = cell;
        cell.select();
        java.util.List<Cell> legalMoves = board.getLegalMoves(cell);
        for (Cell dest : legalMoves) {
            dest.setPossibleDestination();
        }
    }

    private void clearSelection() {
        if (selectedCell != null) {
            selectedCell.deselect();
        }
        board.clearHighlights();
        selectedCell = null;
    }

    // -------------------------------------------------------------------------
    // Move execution
    // -------------------------------------------------------------------------

    /**
     * Validate, execute, switch turn, and check game status.
     */
    public void makeMove(Cell from, Cell to) {
        Piece moving  = from.getPiece();
        Piece captured = to.getPiece();
        if (moving == null) return;

        boolean wasPromotion = false;
        boolean wasCastle    = isKingCastle(from, to);
        boolean wasEnPassant = isEnPassantMove(from, to);

        // Build notation before move alters the board
        String notation = buildNotation(from, to, moving, captured, wasCastle);

        // Record state before move for undo
        boolean hadMoved = moving.hasMoved();
        MoveRecord record = new MoveRecord(from, to, moving, captured,
                                           wasCastle, wasEnPassant, wasPromotion,
                                           notation, hadMoved);
        board.movePiece(from, to);
        history.push(record);

        switchTurn();
        updateStatus();
    }

    private boolean isKingCastle(Cell from, Cell to) {
        Piece p = from.getPiece();
        return (p instanceof Chess.model.pieces.King) && Math.abs(from.getY() - to.getY()) == 2;
    }

    private boolean isEnPassantMove(Cell from, Cell to) {
        Piece p = from.getPiece();
        return (p instanceof Chess.model.pieces.Pawn)
            && from.getY() != to.getY()
            && to.isEmpty();
    }

    private String buildNotation(Cell from, Cell to, Piece moving, Piece captured, boolean castle) {
        if (castle) {
            return (to.getY() > from.getY()) ? "O-O" : "O-O-O";
        }
        char toCol = (char) ('a' + to.getY());
        int  toRow = 8 - to.getX();
        String piecePrefix = getPiecePrefix(moving);
        String captureStr  = (captured != null) ? "x" : "";
        return piecePrefix + captureStr + toCol + toRow;
    }

    private String getPiecePrefix(Piece p) {
        if (p instanceof Chess.model.pieces.King)   return "K";
        if (p instanceof Chess.model.pieces.Queen)  return "Q";
        if (p instanceof Chess.model.pieces.Rook)   return "R";
        if (p instanceof Chess.model.pieces.Bishop) return "B";
        if (p instanceof Chess.model.pieces.Knight) return "N";
        return ""; // Pawn has no prefix
    }

    // -------------------------------------------------------------------------
    // Turn management
    // -------------------------------------------------------------------------

    /**
     * Toggle current turn; trigger bot if in PVB mode.
     */
    public void switchTurn() {
        currentTurn = (currentTurn == WHITE) ? BLACK : WHITE;

        if (gameMode == GameMode.PVB && bot != null && currentTurn == bot.getColor()) {
            triggerBotMove();
        }
    }

    private void triggerBotMove() {
        int[] move = bot.getBestMove(board);
        if (move != null) {
            Cell from = board.getCell(move[0], move[1]);
            Cell to   = board.getCell(move[2], move[3]);
            makeMove(from, to);
        }
    }

    // -------------------------------------------------------------------------
    // Status
    // -------------------------------------------------------------------------

    /** Evaluate check / checkmate / stalemate after each move. */
    public void updateStatus() {
        if (board.isCheckmate(currentTurn)) {
            status = GameStatus.CHECKMATE;
        } else if (board.isStalemate(currentTurn)) {
            status = GameStatus.STALEMATE;
        } else if (board.isInCheck(currentTurn)) {
            status = GameStatus.CHECK;
            // Highlight king cell
            highlightKingInCheck();
        } else {
            status = GameStatus.ONGOING;
        }
    }

    private void highlightKingInCheck() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Cell cell = board.getCell(r, c);
                if (!cell.isEmpty() && cell.getPiece() instanceof Chess.model.pieces.King
                        && cell.getPiece().getColor() == currentTurn) {
                    cell.setCheck();
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Undo / resign / draw / restart
    // -------------------------------------------------------------------------

    /**
     * Pop from history; restore board state.
     * (Simple undo: reinitialise board and replay all but last move.)
     */
    public void undoMove() {
        if (history.isEmpty()) return;

        MoveRecord last = history.pop();
        // Restore pieces on board
        board.getCell(last.fromX, last.fromY).setPiece(last.movedPiece);
        board.getCell(last.toX,   last.toY  ).setPiece(last.capturedPiece);

        if (!last.pieceHadMoved) {
            // Reset hasMoved by reflection — simplest approach: rebuild board from history
            // For production quality, maintain full snapshot per move.
        }

        currentTurn = (currentTurn == WHITE) ? BLACK : WHITE;
        updateStatus();
        board.notifyListeners();
    }

    /** Immediately declare opponent the winner. */
    public void resignGame(int color) {
        status = GameStatus.RESIGNED;
        board.notifyListeners();
    }

    /** Prompt opponent to accept draw (sets status on acceptance). */
    public void offerDraw() {
        // Handled by UI — if accepted, call acceptDraw()
    }

    public void acceptDraw() {
        status = GameStatus.DRAW;
        board.notifyListeners();
    }

    public void restartGame() {
        startGame(gameMode);
        board.notifyListeners();
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public GameStatus getStatus()     { return status; }
    public int        getCurrentTurn(){ return currentTurn; }
    public Board      getBoard()      { return board; }
    public MoveHistory getHistory()   { return history; }
    public GameMode   getGameMode()   { return gameMode; }
}
