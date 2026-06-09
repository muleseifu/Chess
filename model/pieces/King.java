package chess.model.pieces;

import chess.model.Move;
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;
import chess.model.board.ChessBoard;
import chess.model.board.CastlingRights;

import java.util.ArrayList;
import java.util.List;

/**
 * The King piece.
 *
 * <p>Movement rules:
 * <ul>
 *   <li>Moves exactly one square in any direction.</li>
 *   <li>Castling: if neither the king nor the chosen rook has moved,
 *       the squares between them are empty, and the king does not pass
 *       through or land on an attacked square, the king may castle
 *       kingside (O-O) or queenside (O-O-O).</li>
 * </ul>
 * </p>
 */
public class King extends Piece {

    private static final int[][] STEP_DELTAS = {
        {-1, -1}, {-1, 0}, {-1, 1},
        { 0, -1},           { 0, 1},
        { 1, -1}, { 1, 0}, { 1, 1}
    };

    public King(PieceColor color) {
        super(PieceType.KING, color);
    }

    private King(King other) {
        super(other);
    }

    @Override
    public Piece copy() {
        return new King(this);
    }

    @Override
    public List<Move> generatePseudoLegalMoves(Position from, ChessBoard board) {
        List<Move> moves = new ArrayList<>();

        // Normal king steps
        for (int[] delta : STEP_DELTAS) {
            addStepMove(moves, from, from.offset(delta[0], delta[1]), board);
        }

        // Castling
        if (!hasMoved() && from.col == 4) {
            addCastlingMoves(moves, from, board);
        }

        return moves;
    }

    private void addCastlingMoves(List<Move> moves, Position from, ChessBoard board) {
        CastlingRights rights = board.getCastlingRights();
        PieceColor color = getColor();

        // Kingside castling
        if (rights.canCastleKingside(color)) {
            Position rook = new Position(from.row, 7);
            Piece rookPiece = board.getPieceAt(rook);
            if (rookPiece != null && !rookPiece.hasMoved()
                    && board.getPieceAt(new Position(from.row, 5)) == null
                    && board.getPieceAt(new Position(from.row, 6)) == null
                    && !board.isSquareAttackedBy(from, color.opposite())
                    && !board.isSquareAttackedBy(new Position(from.row, 5), color.opposite())
                    && !board.isSquareAttackedBy(new Position(from.row, 6), color.opposite())) {
                moves.add(Move.kingsideCastle(from, new Position(from.row, 6), this));
            }
        }

        // Queenside castling
        if (rights.canCastleQueenside(color)) {
            Position rook = new Position(from.row, 0);
            Piece rookPiece = board.getPieceAt(rook);
            if (rookPiece != null && !rookPiece.hasMoved()
                    && board.getPieceAt(new Position(from.row, 1)) == null
                    && board.getPieceAt(new Position(from.row, 2)) == null
                    && board.getPieceAt(new Position(from.row, 3)) == null
                    && !board.isSquareAttackedBy(from, color.opposite())
                    && !board.isSquareAttackedBy(new Position(from.row, 3), color.opposite())
                    && !board.isSquareAttackedBy(new Position(from.row, 2), color.opposite())) {
                moves.add(Move.queensideCastle(from, new Position(from.row, 2), this));
            }
        }
    }
}
