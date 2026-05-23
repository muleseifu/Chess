package Chess.model.pieces;

import Chess.model.board.Board;
import Chess.model.board.Cell;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    boolean enPassantVulnerable = false;

    public Pawn(String id, int color, String imagePath) {
        super(id, color, imagePath);
    }

    @Override
    public List<Cell> move(Cell pos, Board board) {

        List<Cell> legalMoves = new ArrayList<>();

        int direction = (this.color == 0) ? -1 : 1;

        // FIXED
        int startRow = (this.color == 0) ? 6 : 1;

        int x = pos.getX();
        int y = pos.getY();

        // =========================
        // Forward movement
        // =========================

        int newX = x + direction;

        if (newX >= 0 && newX < 8) {

            Cell forwardOne = board.getCell(newX, y);

            if (forwardOne.isEmpty()) {

                legalMoves.add(forwardOne);

                // 2-square move
                if (x == startRow) {

                    int newXTwo = x + (2 * direction);

                    Cell forwardTwo = board.getCell(newXTwo, y);

                    if (forwardTwo.isEmpty()) {
                        legalMoves.add(forwardTwo);
                    }
                }
            }
        }

        // =========================
        // Normal diagonal captures
        // =========================

        int[] captureY = {-1, 1};

        for (int dy : captureY) {

            int captureX = x + direction;
            int captureYPos = y + dy;

            if (
                captureX >= 0 &&
                captureX < 8 &&
                captureYPos >= 0 &&
                captureYPos < 8
            ) {

                Cell target = board.getCell(captureX, captureYPos);

                if (!target.isEmpty() && target.isEnemy(this.color)) {
                    legalMoves.add(target);
                }
            }
        }

        // =========================
        // En Passant
        // =========================

        for (int dy : captureY) {

            int sideY = y + dy;

            if (sideY >= 0 && sideY < 8) {

                Cell adjacent = board.getCell(x, sideY);

                if (canEnPassant(adjacent)) {

                    Cell enPassantMove =
                            board.getCell(x + direction, sideY);

                    legalMoves.add(enPassantMove);
                }
            }
        }

        return legalMoves;
    }

    public Piece promote(String choice, Board board) {

        switch (choice.toLowerCase()) {

            case "queen":
                return new Queen("Q", color, "queen.png");

            case "rook":
                return new Rook("R", color, "/");

            case "bishop":
                return new Bishop("B", color, "/");

            case "knight":
                return new Knight("N", color, "knight.png");

            default:
                return new Queen("Q", color, "queen.png");
        }
    }

    public boolean canEnPassant(Cell target) {

        if (target == null || target.isEmpty()) {
            return false;
        }

        if (!(target.getPiece() instanceof Pawn)) {
            return false;
        }

        if (!target.isEnemy(this.color)) {
            return false;
        }

        Pawn enemyPawn = (Pawn) target.getPiece();

        return enemyPawn.enPassantVulnerable;
    }

    @Override
    public Piece getCopy() {

        Pawn copy = new Pawn(this.id, this.color, this.imagePath);

        copy.enPassantVulnerable = this.enPassantVulnerable;
        copy.availability = this.availability;
        copy.hasMoved = this.hasMoved;

        return copy;
    }
}