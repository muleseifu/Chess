package Chess.model.pieces;

import Chess.model.board.Board;
import Chess.model.board.Cell;
import java.util.ArrayList;
import java.util.List;
public class King extends Piece {
    int x=pos.getX();
    int y=pos.getY();
    public Kight(int color,String id,String imagePath){
        super(color,id,imagePath);
    }
    @Override
     public List<Cell> move(Cell pos, Board board) {
        List<Cell> destination=new ArrayList<>();
        int[][] destination={
            {-1, -1}, {-1, 0}, {-1, +1},
            { 0, -1},           { 0, +1},
            {+1, -1}, {+1, 0}, {+1, +1}
        };
        for (int[] dir : destination) {
            int row = pos.getX() + dir[0];
            int col = pos.getY() + dir[1];
            if (row >= 0 && row < 8 && col >= 0 && col < 8){
                Cell target = board.getCell(row, col);
                if (target.isEmpty() || target.isEnemy(this.color)) {
                    if (!wouldBeInDanger(board, row, col)) {
                        destinations.add(target);
                    }
                }
            }
        }
        if (canCastleKingside(board)) {
            destinations.add(board.getCell(pos.getX(), pos.getY() + 2));
        }
        if (canCastleQueenside(board)) {
            destinations.add(board.getCell(pos.getX(), pos.getY() - 2));
        }
     return distinations;
     }
      public boolean isInDanger(Board board){
        return board.isInCheck(this.color);
    }
        public boolean canCastleKingSide(Board board) {
        if (this.hasMoved || isInDanger(board)) return false;
 
        Cell rookCell = board.getCell(this.x, 7);
        if (rookCell.isEmpty()) return false;
        Piece rook = rookCell.getPiece();
        if (!(rook instanceof Rook) || rook.hasMoved() || rook.getColor() != this.color) return false;
 
        // Squares between must be empty
        if (!board.getCell(this.x, 5).isEmpty() || !board.getCell(this.x, 6).isEmpty()) return false;
 
        // King must not pass through or land on an attacked square
        return !board.isInCheck(this.color);
    }
    
    public boolean canCastleQueenSide(Board board) {
        if (this.hasMoved || isInDanger(board)) return false;
 
        Cell rookCell = board.getCell(this.x, 0);
        if (rookCell.isEmpty()) return false;
        Piece rook = rookCell.getPiece();
        if (!(rook instanceof Rook) || rook.hasMoved() || rook.getColor() != this.color) return false;
 
        // Squares between must be empty (cols 1, 2, 3)
        if (!board.getCell(this.x, 1).isEmpty() ||
            !board.getCell(this.x, 2).isEmpty() ||
            !board.getCell(this.x, 3).isEmpty()) return false;
 
        return !board.isInCheck(this.color);
    }
 
    @Override
    public Piece getCopy() {
        King copy = new King(this.color);
        copy.setId(this.id);
        copy.setPath(this.imagePath);
        copy.setAvailable(this.availability);
        copy.x = this.x;
        copy.y = this.y;
        if (this.hasMoved) copy.setMoved();
        return copy;
    }
 
    @Override
    public String toString() {
        return "King[" + (color == 0 ? "White" : "Black") + "]";
    }
}
