package Chess.model.game;

import Chess.ai.Bot;
import Chess.model.board.Board;
import Chess.model.board.Cell;
import Chess.model.pieces.Pawn;
import Chess.model.pieces.Piece;
import Chess.util.Constants.*;
import Chess.util.MoveHistory;
import Chess.util.MoveRecord;

import java.util.List;

public class Game {

    private Board board;
    private int currentTurn; // 0 = White, 1 = Black
    private GameMode gameMode;
    private Bot bot;
    private MoveHistory history;
    private GameStatus status;

    private Cell selectedCell;
    private boolean isBotMoving = false;

    public Game() {
        this.history = new MoveHistory();
        this.status = GameStatus.ONGOING;
        this.selectedCell = null;
    }

    // --------------------------------------------------

    public void startGame(GameMode mode) {
        this.gameMode = mode;
        this.currentTurn = 0;
        this.status = GameStatus.ONGOING;
        this.selectedCell = null;
        history.clear();

        board = new Board();
        board.initBoard();
        board.placePieces();

        if (mode == GameMode.PVB) {
            bot = new Bot(1, Difficulty.MEDIUM);
        } else {
            bot = null;
        }
    }

    // --------------------------------------------------

    public void handleCellClick(Cell cell) {

        if (status == GameStatus.CHECKMATE ||
            status == GameStatus.STALEMATE ||
            status == GameStatus.DRAW ||
            status == GameStatus.RESIGNED) {
            return;
        }

        if (gameMode == GameMode.PVB &&
            currentTurn == 1 &&
            isBotMoving) {
            return;
        }

        if (selectedCell == null) {

            if (!cell.isEmpty() &&
                cell.getPiece().getColor() == currentTurn) {

                selectedCell = cell;
                cell.select();

                List<Cell> moves = board.getLegalMoves(cell);
                for (Cell c : moves) {
                    c.setPossibleDestination();
                }
            }

        } else {

            if (cell == selectedCell) {
                clearSelection();
                return;
            }

            if (!cell.isEmpty() &&
                cell.getPiece().getColor() == currentTurn) {

                clearSelection();
                selectedCell = cell;
                cell.select();

                List<Cell> moves = board.getLegalMoves(cell);
                for (Cell c : moves) {
                    c.setPossibleDestination();
                }
                return;
            }

            List<Cell> legal = board.getLegalMoves(selectedCell);

            if (legal.contains(cell)) {
                makeMove(selectedCell, cell);
                clearSelection();
            } else {
                clearSelection();
            }
        }
    }

    // --------------------------------------------------

    public void makeMove(Cell from, Cell to) {

        Piece movedPiece = from.getPiece();
        Piece capturedPiece = to.getPiece();

        boolean isCastle = false;
        boolean isEnPassant = false;
        boolean isPromotion = false;

        if (movedPiece instanceof Chess.model.pieces.King) {
            int colDiff = Math.abs(to.getY() - from.getY());
            if (colDiff == 2) isCastle = true;
        }

        if (movedPiece instanceof Pawn) {

            int colDiff = Math.abs(to.getY() - from.getY());
            if (colDiff == 1 && to.isEmpty()) {
                isEnPassant = true;
            }

            int promoRow = (movedPiece.getColor() == 0) ? 0 : 7;
            if (to.getX() == promoRow) {
                isPromotion = true;
            }
        }

        String notation = buildNotation(from, to, movedPiece,
                capturedPiece, isCastle, isEnPassant, isPromotion);

        board.movePiece(from, to);

        MoveRecord record = new MoveRecord(
                from, to, movedPiece, capturedPiece,
                isCastle, isEnPassant, isPromotion, notation
        );

        history.push(record);

        updateStatus();
        board.notifyListeners();

        switchTurn();
    }

    // --------------------------------------------------

    public void switchTurn() {

        currentTurn = (currentTurn == 0) ? 1 : 0;

        if (gameMode == GameMode.PVB &&
            bot != null &&
            currentTurn == bot.getColor() &&
            !isBotMoving) {

            isBotMoving = true;

            int[] move = bot.getBestMove(board);

            if (move != null) {
                Cell from = board.getCell(move[0], move[1]);
                Cell to   = board.getCell(move[2], move[3]);

                makeMove(from, to);
            }

            isBotMoving = false;
        }
    }

    // --------------------------------------------------

    public void updateStatus() {

        int next = (currentTurn == 0) ? 1 : 0;

        if (board.isCheckmate(next)) {
            status = GameStatus.CHECKMATE;
        } else if (board.isStalemate(next)) {
            status = GameStatus.STALEMATE;
        } else if (board.isInCheck(next)) {
            status = GameStatus.CHECK;
        } else {
            status = GameStatus.ONGOING;
        }
    }

    // --------------------------------------------------

    private void clearSelection() {
        if (selectedCell != null) selectedCell.deselect();
        board.clearHighlights();
        selectedCell = null;
    }

    // --------------------------------------------------

    private String buildNotation(Cell from, Cell to,
                                 Piece movedPiece,
                                 Piece capturedPiece,
                                 boolean isCastle,
                                 boolean isEnPassant,
                                 boolean isPromotion) {

        if (isCastle) {
            return (to.getY() > from.getY()) ? "O-O" : "O-O-O";
        }

        StringBuilder sb = new StringBuilder();

        String symbol = getPieceSymbol(movedPiece);
        sb.append(symbol);

        if (capturedPiece != null || isEnPassant) sb.append("x");

        sb.append((char) ('a' + to.getY()));
        sb.append(8 - to.getX());

        if (isPromotion) sb.append("=Q");

        return sb.toString();
    }

    private String getPieceSymbol(Piece piece) {
        String name = piece.getClass().getSimpleName();

        switch (name) {
            case "King": return "K";
            case "Queen": return "Q";
            case "Rook": return "R";
            case "Bishop": return "B";
            case "Knight": return "N";
            default: return "";
        }
    }

    // --------------------------------------------------

    public Board getBoard() { return board; }
    public GameStatus getStatus() { return status; }
    public int getCurrentTurn() { return currentTurn; }
}