package Chess.model.board;
 
//import Chess.gui.BoardListener;
import Chess.model.pieces.*;
import Chess.gui.BoardListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
 
public class Board {
 
    private Cell[][] cells;
    private List<Piece> whitePieces;
    private List<Piece> blackPieces;
    private King whiteKing;
    private King blackKing;
    private List<BoardListener> listeners;
 
    public Board() {
        cells = new Cell[8][8];
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        listeners = new ArrayList<>();
    }
 
    public void initBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Color base = ((row + col) % 2 == 0) ? new Color(240, 217, 181) : new Color(181, 136, 99);
                cells[row][col] = new Cell(row, col, base);
            }
        }
    }
 
    public void placePieces() {
        // Black pieces (row 0-1)
        placeMajorPieces(0, 1); // black
        // White pieces (row 6-7)
        placeMajorPieces(7, 0); // white
    }
 
    private void placeMajorPieces(int backRow, int color) {
        String side = (color == 0) ? "W" : "B";
 
        Rook rook1 = new Rook(color);
        rook1.setId(side + "_R1");
        cells[backRow][0].setPiece(rook1);
 
        Knight knight1 = new Knight(color);
        knight1.setId(side + "_N1");
        cells[backRow][1].setPiece(knight1);

 
        Bishop bishop1 = new Bishop(color);
        bishop1.setId(side + "_B1");
        cells[backRow][2].setPiece(bishop1);
 
        Queen queen = new Queen(color);
        queen.setId(side + "_Q");
        cells[backRow][3].setPiece(queen);
 
        King king = new King(color);
        king.setId(side + "_K");
        king.x = backRow;
        king.y = 4;
        cells[backRow][4].setPiece(king);
 
        Bishop bishop2 = new Bishop(color);
        bishop2.setId(side + "_B2");
        cells[backRow][5].setPiece(bishop2);
 
        Knight knight2 = new Knight(color);
        knight2.setId(side + "_N2");
        cells[backRow][6].setPiece(knight2);
 
        Rook rook2 = new Rook(color);
        rook2.setId(side + "_R2");
        cells[backRow][7].setPiece(rook2);
 
        // Pawns
        int pawnRow = (color == 0) ? 6 : 1;
        for (int col = 0; col < 8; col++) {
            Pawn pawn = new Pawn(color);
            pawn.setId(side + "_P" + col);
            cells[pawnRow][col].setPiece(pawn);
        }
 
        // Track pieces
        List<Piece> pieceList = (color == 0) ? whitePieces : blackPieces;
        pieceList.add(rook1);
        pieceList.add(knight1);
        pieceList.add(bishop1);
        pieceList.add(queen);
        pieceList.add(king);
        pieceList.add(bishop2);
        pieceList.add(knight2);
        pieceList.add(rook2);
        for (int col = 0; col < 8; col++) {
            pieceList.add(cells[pawnRow][col].getPiece());
        }
 
        if (color == 0) {
            whiteKing = king;
        } else {
            blackKing = king;
        }
    }
 
    public Cell getCell(int x, int y) {
        return cells[x][y];
    }
 
    public void movePiece(Cell from, Cell to) {
        Piece piece = from.getPiece();
        if (piece == null) return;
 
        // En passant: clear vulnerable flags before move, then set if pawn double-advance
        clearEnPassantFlags(piece.getColor());
 
        // Handle castling

        if (piece instanceof King) {
            int colDiff = to.getY() - from.getY();
            if (Math.abs(colDiff) == 2) {
                // Determine rook positions
                int row = from.getX();
                if (colDiff == 2) {
                    // King-side
                    Piece rook = cells[row][7].getPiece();
                    if (rook instanceof Rook) {
                        performCastle(from, cells[row][7], (King) piece, (Rook) rook);
                        return;
                    }
                } else {
                    // Queen-side
                    Piece rook = cells[row][0].getPiece();
                    if (rook instanceof Rook) {
                        performCastle(from, cells[row][0], (King) piece, (Rook) rook);
                        return;
                    }
                }
            }
            King king = (King) piece;
            king.x = to.getX();
            king.y = to.getY();
        }
 
        // Handle en passant capture
        if (piece instanceof Pawn) {
                int colDiff = to.getY() - from.getY();
                int rowDiff = to.getX() - from.getX();

                // En passant
                if (Math.abs(colDiff) == 1 && to.isEmpty()) {
                    performEnPassant(from, to, (Pawn) piece);
                    return;
                }

                // Double advance
                if (Math.abs(rowDiff) == 2) {
                    ((Pawn) piece).enPassantVulnerable = true;
    }
}
 
        // Standard move
        if (!to.isEmpty()) {
            Piece captured = to.getPiece();
            captured.setAvailable(false);
            removeFromList(captured);
        }
 
        from.removePiece();
        to.setPiece(piece);
        piece.setMoved();
 
        // Pawn promotion
        if (piece instanceof Pawn) {
            int promotionRow = (piece.getColor() == 0) ? 0 : 7;
            if (to.getX() == promotionRow) {
                // Promotion handled externally by Game; placeholder
                // Game will call showPromotionDialog and replace the piece
            }
        }
 
        notifyListeners();
    }
 
    public List<Cell> getLegalMoves(Cell cell) {
        Piece piece = cell.getPiece();
        if (piece == null) return new ArrayList<>();
 
        List<Cell> candidates = piece.move(cell, this);
        List<Cell> legal = new ArrayList<>();
 
        for (Cell target : candidates) {
            // Simulate move on cloned board
            Board clone = cloneBoard();
            Cell cloneFrom = clone.getCell(cell.getX(), cell.getY());
            Cell cloneTo = clone.getCell(target.getX(), target.getY());
 
            // Execute the move on clone
            Piece clonePiece = cloneFrom.getPiece();
            if (!cloneTo.isEmpty()) {
                clone.removeFromList(cloneTo.getPiece());
                cloneTo.getPiece().setAvailable(false);
            }
            cloneFrom.removePiece();
            cloneTo.setPiece(clonePiece);
 
            // King coordinate update on clone
            if (clonePiece instanceof King) {
                ((King) clonePiece).x = cloneTo.getX();
                ((King) clonePiece).y = cloneTo.getY();
            }
 
            // Check if own king is in check after move
            if (!clone.isInCheck(piece.getColor())) {
                legal.add(target);
            }
        }
        return legal;
    }
 
    public boolean isInCheck(int color) {
        King king = (color == 0) ? whiteKing : blackKing;
        if (king == null) return false;
        return king.isInDanger(this);
    }
 
    public boolean isCheckmate(int color) {
        if (!isInCheck(color)) return false;
        return hasNoLegalMoves(color);
    }
 
    public boolean isStalemate(int color) {
        if (isInCheck(color)) return false;
        return hasNoLegalMoves(color);
    }
 
    private boolean hasNoLegalMoves(int color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Cell c = cells[row][col];
                if (!c.isEmpty() && c.getPiece().getColor() == color) {
                    if (!getLegalMoves(c).isEmpty()) return false;
                }
            }
        }
        return true;
    }
 
    public Board cloneBoard() {
        Board clone = new Board();
        clone.cells = new Cell[8][8];
        clone.whitePieces = new ArrayList<>();
        clone.blackPieces = new ArrayList<>();
        clone.listeners = new ArrayList<>();
 
        // Copy cells and pieces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                clone.cells[row][col] = new Cell(row, col, cells[row][col].getBaseColor());
                Piece original = cells[row][col].getPiece();
                if (original != null) {
                    Piece copied = original.getCopy();
                    clone.cells[row][col].setPiece(copied);
                    if (original.getColor() == 0) {
                        clone.whitePieces.add(copied);
                        if (copied instanceof King) clone.whiteKing = (King) copied;
                    } else {
                        clone.blackPieces.add(copied);
                        if (copied instanceof King) clone.blackKing = (King) copied;
                    }
                }
            }
        }
        return clone;
    }
 
    public void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                cells[row][col].deselect();
                cells[row][col].removePossibleDestination();
                cells[row][col].removeCheck();
            }
        }
    }
 
    public void notifyListeners() {
        for (BoardListener listener : listeners) {
            listener.onBoardChanged();
        }
    }
 
    public void addListener(BoardListener l) {
        listeners.add(l);
    }
 
    public void performCastle(Cell kingCell, Cell rookCell,
                          King king, Rook rook) {

                    int row = kingCell.getX();
                    boolean kingSide = rookCell.getY() > kingCell.getY();

                    int kingTargetCol = kingSide ? 6 : 2;
                    int rookTargetCol = kingSide ? 5 : 3;

                    kingCell.removePiece();
                    rookCell.removePiece();

                    cells[row][kingTargetCol].setPiece(king);
                    cells[row][rookTargetCol].setPiece(rook);

                    king.setMoved();
                    rook.setMoved();

                    notifyListeners();
}
 
    public void performEnPassant(Cell from, Cell to, Pawn pawn) {

    // Captured pawn is beside the moving pawn
    Cell capturedPawnCell = cells[from.getX()][to.getY()];

    Piece capturedPawn = capturedPawnCell.getPiece();

    if (capturedPawn != null) {
        capturedPawn.setAvailable(false);
        removeFromList(capturedPawn);
        capturedPawnCell.removePiece();
    }

    from.removePiece();
    to.setPiece(pawn);

    pawn.setMoved();

    notifyListeners();
}
 
    // ---- Accessors for pieces ----
 
    public List<Piece> getWhitePieces() {
        return whitePieces;
    }
 
    public List<Piece> getBlackPieces() {
        return blackPieces;
    }
 
    public King getWhiteKing() {
        return whiteKing;
    }
 
    public King getBlackKing() {
        return blackKing;
    }
 
    // ---- Private helpers ----
 
    private void removeFromList(Piece piece) {
        whitePieces.remove(piece);
        blackPieces.remove(piece);
    }
 
    private void clearEnPassantFlags(int exceptColor) {
        // Clear en passant flags for opponent's pawns (they expire after one turn)
        int opponentColor = (exceptColor == 0) ? 1 : 0;
        List<Piece> opponentPieces = (opponentColor == 0) ? whitePieces : blackPieces;
        for (Piece p : opponentPieces) {
            if (p instanceof Pawn) {
                ((Pawn) p).enPassantVulnerable = false;
            }
        }
    }
}
