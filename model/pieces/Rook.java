package Chess.model.pieces;

import Chess.model.board.Board;
import Chess.model.board.Cell;
import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public Rook(int color) {
        super();
        this.color = color;
    }

    public Rook(String id, int color, String imagePath) {
        super(id, color, imagePath);

    }

    @Override
    public List<Cell> move(Cell pos, Board board) {
        List<Cell> legalMoves = new ArrayList<>();
        // 4 Cardinal directions: Down, Up, Right, Left
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        for (int i = 0; i < 4; i++) {
            int x = pos.getX() + dx[i];
            int y = pos.getY() + dy[i];

            while (x >= 0 && x < 8 && y >= 0 && y < 8) {
                Cell target = board.getCell(x, y);
                if (target.isEmpty()) {
                    legalMoves.add(target);
                } else {
                    if (target.isEnemy(this.color)) {
                        legalMoves.add(target); // Include capture [cite: 67]
                    }
                    break; // Move blocked by any occupied square [cite: 67]
                }
                x += dx[i];
                y += dy[i];
            }
        }
        return legalMoves;
    }

    @Override
    public Piece getCopy() {
        Rook copy = new Rook(this.id, this.color, this.imagePath);
        copy.availability = this.availability;
        copy.hasMoved = this.hasMoved;
        return copy;
    }

    public boolean canCastle() {
        return !hasMoved; // Returns !hasMoved for castling eligibility [cite: 67]
    }
}
