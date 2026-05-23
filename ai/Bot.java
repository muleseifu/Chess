package Chess.ai;

import Chess.model.board.Board;
import Chess.model.board.Cell;
import Chess.model.pieces.*;
import Chess.util.Constants.Difficulty;

import java.util.ArrayList;
import java.util.List;

public class Bot {

    private int color;
    private int depth;

    public Bot(int color, Difficulty difficulty) {
        this.color = color;
        this.depth = difficulty.depth;
    }


    public int getColor() {
        return this.color;
    }



    // --------------------------------------------------
    // GET ALL LEGAL MOVES (IMPORTANT FIX)
    // --------------------------------------------------

    private List<int[]> getAllMoves(Board board, int color) {
        List<int[]> moves = new ArrayList<>();

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                Cell from = board.getCell(r, c);

                if (!from.isEmpty() &&
                    from.getPiece().getColor() == color) {

                    List<Cell> targets = board.getLegalMoves(from);

                    for (Cell to : targets) {
                        moves.add(new int[]{
                                r, c,
                                to.getX(), to.getY()
                        });
                    }
                }
            }
        }
        return moves;
    }

    // --------------------------------------------------
    // BEST MOVE
    // --------------------------------------------------

    public int[] getBestMove(Board board) {

        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        List<int[]> moves = getAllMoves(board, color);

        for (int[] move : moves) {

            Board sim = board.cloneBoard();

            Cell from = sim.getCell(move[0], move[1]);
            Cell to   = sim.getCell(move[2], move[3]);

            sim.movePiece(from, to);

            int score = minimax(sim, depth - 1,
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE,
                    false);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    // --------------------------------------------------
    // MINIMAX
    // --------------------------------------------------

    private int minimax(Board board, int depth,
                        int alpha, int beta,
                        boolean maximising) {

        if (depth == 0 ||
            board.isCheckmate(color) ||
            board.isCheckmate(1 - color) ||
            board.isStalemate(color) ||
            board.isStalemate(1 - color)) {

            return evaluate(board);
        }

        int currentColor = maximising ? color : 1 - color;
        List<int[]> moves = getAllMoves(board, currentColor);

        if (maximising) {

            int maxEval = Integer.MIN_VALUE;

            for (int[] move : moves) {

                Board sim = board.cloneBoard();

                sim.movePiece(
                        sim.getCell(move[0], move[1]),
                        sim.getCell(move[2], move[3])
                );

                int eval = minimax(sim, depth - 1,
                        alpha, beta, false);

                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);

                if (beta <= alpha) break;
            }

            return maxEval;
        } else {

            int minEval = Integer.MAX_VALUE;

            for (int[] move : moves) {

                Board sim = board.cloneBoard();

                sim.movePiece(
                        sim.getCell(move[0], move[1]),
                        sim.getCell(move[2], move[3])
                );

                int eval = minimax(sim, depth - 1,
                        alpha, beta, true);

                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);

                if (beta <= alpha) break;
            }

            return minEval;
        }
    }

    // --------------------------------------------------
    // EVALUATION
    // --------------------------------------------------

    private int evaluate(Board board) {
        int score = 0;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                Cell cell = board.getCell(r, c);

                if (!cell.isEmpty()) {

                    Piece p = cell.getPiece();
                    int value = material(p);

                    if (p.getColor() == color) score += value;
                    else score -= value;
                }
            }
        }

        return score;
    }

    private int material(Piece p) {
        if (p instanceof Queen) return 900;
        if (p instanceof Rook) return 500;
        if (p instanceof Bishop) return 330;
        if (p instanceof Knight) return 320;
        if (p instanceof Pawn) return 100;
        if (p instanceof King) return 20000;
        return 0;
    }

     public void setDifficulty(Difficulty difficulty) {
        this.depth = difficulty.depth;
    }
}