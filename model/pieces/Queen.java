package Chess.model.pieces;

import java.util.ArrayList;
import java.util.List;

import Chess.model.board.Board;
import Chess.model.board.Cell;

public class Queen extends Piece {

    public Queen(String id, int color, String imagePath) {
        super(id, color, imagePath);
    }

    @Override
    public Piece getCopy() {

        Queen copy = new Queen(this.id, this.color, this.imagePath);
        copy.availability = this.availability;
        copy.hasMoved = this.hasMoved;

        return copy;
    }

    @Override
    public List<Cell> move(Cell pos, Board board) {

        List<Cell> legalMoves = new ArrayList<>();

        int x = pos.getX();
        int y = pos.getY();

        // Queen moves in 8 directions
        int[][] directions = {

            // Horizontal & Vertical
            {-1, 0},
            { 1, 0},
            { 0,-1},
            { 0, 1},

            // Diagonal
            {-1,-1},
            {-1, 1},
            { 1,-1},
            { 1, 1}
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

                // Empty square
                if (target.isEmpty()) {

                    legalMoves.add(target);

                } else {

                    // Enemy piece
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
}