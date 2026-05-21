package Chess.model.pieces;

import Chess.model.board.Board;
import Chess.model.board.Cell;
import java.util.List;
import java.util.ArrayList;

public class Bishop extends Piece {

    public Bishop(String id, int color, String imagePath) {
        super(id, color, imagePath);
    }

    @Override
    public List<Cell> move(Cell pos, Board board) {

        List<Cell> legalMoves = new ArrayList<>();

        int x = pos.getX();
        int y = pos.getY();

        // 4 diagonal directions
        int[][] directions = {
            {-1, -1}, // up-left
            {-1,  1}, // up-right
            { 1, -1}, // down-left
            { 1,  1}  // down-right
        };

        for (int[] dir : directions) {

            int dx = dir[0];
            int dy = dir[1];

            int currentX = x + dx;
            int currentY = y + dy;

            while (
                currentX >= 0 &&
                currentX < 8 &&
                currentY >= 0 &&
                currentY < 8
            ) {

                Cell target = board.getCell(currentX, currentY);

                // Empty square -> bishop can continue
                if (target.isEmpty()) {

                    legalMoves.add(target);

                } else {

                    // Enemy piece -> can capture
                    if (target.isEnemy(this.color)) {
                        legalMoves.add(target);
                    }

                    // Stop after hitting any piece
                    break;
                }

                currentX += dx;
                currentY += dy;
            }
        }

        return legalMoves;
    }

    @Override
    public Piece getCopy() {

        return new Bishop(this.id, this.color, this.imagePath);
    }
}