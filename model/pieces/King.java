package Chess.model.pieces;

import Chess.model.board.Board;
import Chess.model.board.Cell;
import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public int x, y; // King tracks its own position for check detection


    public King(int color) {
        super();
        this.color = color;
    }

    public King(String id, int color, String imagePath) {
        super(id, color, imagePath);
    }

    @Override
    public List<Cell> move(Cell pos, Board board) {
        List<Cell> legalMoves = new ArrayList<>();
        int[] dx = {1, 1, 1, 0, 0, -1, -1, -1};
        int[] dy = {1, 0, -1, 1, -1, 1, 0, -1};

        for (int i = 0; i < 8; i++) {
            int newX = pos.getX() + dx[i];
            int newY = pos.getY() + dy[i];

            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                Cell target = board.getCell(newX, newY);
                if (target.isEmpty() || target.isEnemy(this.color)) {
                    legalMoves.add(target);
                }
            }
        }

        // Castling logic (Simplified: check moved status and clear path)
        if (!hasMoved) {
            int row = pos.getX();
            // King-side
            Piece rookK = board.getCell(row, 7).getPiece();
            if (rookK instanceof Rook && !rookK.hasMoved()) {
                if (board.getCell(row, 5).isEmpty() && board.getCell(row, 6).isEmpty()) {
                    legalMoves.add(board.getCell(row, 6));
                }
            }
            // Queen-side
            Piece rookQ = board.getCell(row, 0).getPiece();
            if (rookQ instanceof Rook && !rookQ.hasMoved()) {
                if (board.getCell(row, 1).isEmpty() && board.getCell(row, 2).isEmpty() && board.getCell(row, 3).isEmpty()) {
                    legalMoves.add(board.getCell(row, 2));
                }
            }
        }

        return legalMoves;
    }

    public boolean isInDanger(Board board) {
        // Check if any enemy piece can move to (x, y)
        List<Piece> enemies = (color == 0) ? board.getBlackPieces() : board.getWhitePieces();
        for (Piece p : enemies) {
            if (!p.isAvailable()) continue;
            
            // Find the cell containing this piece
            Cell pieceCell = null;
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    if (board.getCell(r, c).getPiece() == p) {
                        pieceCell = board.getCell(r, c);
                        break;
                    }
                }
            }

            if (pieceCell != null) {
                List<Cell> moves = p.move(pieceCell, board);
                for (Cell m : moves) {
                    if (m.getX() == this.x && m.getY() == this.y) return true;
                }
            }
        }
        return false;
    }

    @Override
    public Piece getCopy() {
        King copy = new King(this.id, this.color, this.imagePath);
        copy.x = this.x;
        copy.y = this.y;
        copy.hasMoved = this.hasMoved;
        copy.availability = this.availability;
        return copy;
    }
}