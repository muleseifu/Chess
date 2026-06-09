package chess.model.board;

import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.pieces.Piece;
import chess.model.pieces.PieceFactory;

/**
 * Responsible for setting up the initial piece arrangement on the board.
 * Separates board-initialisation logic from board-state logic (SRP).
 */
public class BoardSetup {

    private BoardSetup() {}

    private static final PieceType[] BACK_RANK = {
        PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN,
        PieceType.KING, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK
    };

    /**
     * Fill the given 8×8 array with the standard starting position.
     * Row 0 = rank 8 (black back rank), Row 7 = rank 1 (white back rank).
     */
    public static void placeInitialPieces(Piece[][] squares) {
        for (int col = 0; col < 8; col++) {
            squares[0][col] = PieceFactory.create(BACK_RANK[col], PieceColor.BLACK);
            squares[1][col] = PieceFactory.create(PieceType.PAWN,  PieceColor.BLACK);
            squares[6][col] = PieceFactory.create(PieceType.PAWN,  PieceColor.WHITE);
            squares[7][col] = PieceFactory.create(BACK_RANK[col],  PieceColor.WHITE);
        }
    }
}
