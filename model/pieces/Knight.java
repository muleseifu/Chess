package Chess.model.pieces;

import Chess.model.board.Board;
import Chess.model.board.Cell;
import java.util.List;
import java.util.ArrayList;

public class Knight extends Piece {

    public Knight(String id, int color, String imagePath) {
        super(id, color, imagePath);
    }

    @Override
    public List<Cell> move(Cell pos, Board board) {

        List<Cell> legalMoves = new ArrayList<>();

        int x = pos.getX();
        int y = pos.getY();

        // All 8 possible knight moves
        int[][] moves = {
            {-2, -1},
            {-2,  1},
            {-1, -2},
            {-1,  2},
            { 1, -2},
            { 1,  2},
            { 2, -1},
            { 2,  1}
        };

        for (int[] move : moves) {

            int newX = x + move[0];
            int newY = y + move[1];

            // Stay inside board
            if (
                newX >= 0 &&
                newX < 8 &&
                newY >= 0 &&
                newY < 8
            ) {

                Cell target = board.getCell(newX, newY);

                // Empty OR enemy square
                if (
                    target.isEmpty() ||
                    target.isEnemy(this.color)
                ) {
                    legalMoves.add(target);
                }
            }
        }

        return legalMoves;
    }

    @Override
    public Piece getCopy() {

        Knight copy = new Knight(this.id, this.color, this.imagePath)

        copy.availability = this.availability;
        copy.hasMoved = this.hasMoved;

        return copy;
    }
}