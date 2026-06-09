package chess.ui.renderer;

import chess.model.Move;
import chess.model.PieceColor;
import chess.model.Position;
import chess.model.board.Board;
import chess.model.pieces.Piece;
import chess.model.state.GameState;

import java.awt.*;
import java.util.List;

/**
 * Orchestrates painting of the full chess board onto a {@link Graphics2D} context.
 *
 * <p>Painting order (back to front):
 * <ol>
 *   <li>Square base colors</li>
 *   <li>Last-move highlights</li>
 *   <li>Check highlight on the king in check</li>
 *   <li>Selected-square highlight</li>
 *   <li>Legal-move indicators</li>
 *   <li>Pieces</li>
 *   <li>Rank/file labels</li>
 * </ol>
 * </p>
 */
public class BoardRenderer {

    private final int squareSize;

    public BoardRenderer(int squareSize) {
        this.squareSize = squareSize;
    }

    public void paint(Graphics2D g2, Board board,
                      Position selected, List<Move> legalMoves,
                      boolean flipped) {

        Move lastMove = board.getLastMove();
        GameState state = board.getGameState();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int drawRow = flipped ? 7 - row : row;
                int drawCol = flipped ? 7 - col : col;

                int x = drawCol * squareSize;
                int y = drawRow * squareSize;

                Position pos = new Position(row, col);
                boolean isLight = (row + col) % 2 == 0;

                // 1. Base square
                SquareRenderer.drawSquare(g2, x, y, squareSize, isLight);

                // 2. Last move
                if (lastMove != null &&
                    (pos.equals(lastMove.getFrom()) || pos.equals(lastMove.getTo()))) {
                    SquareRenderer.drawLastMoveHighlight(g2, x, y, squareSize);
                }

                // 3. Check highlight
                if ((state == GameState.CHECK || state == GameState.CHECKMATE)) {
                    PieceColor checked = board.getCurrentTurn();
                    Position kingPos   = board.findKing(checked);
                    if (pos.equals(kingPos)) {
                        SquareRenderer.drawCheckHighlight(g2, x, y, squareSize);
                    }
                }

                // 4. Selected square
                if (pos.equals(selected)) {
                    SquareRenderer.drawSelectedHighlight(g2, x, y, squareSize);
                }

                // 5. Legal-move indicators
                if (legalMoves != null) {
                    for (Move m : legalMoves) {
                        if (m.getTo().equals(pos)) {
                            Piece target = board.getPieceAt(pos);
                            if (target != null && !m.isEnPassant()) {
                                SquareRenderer.drawLegalCaptureRing(g2, x, y, squareSize);
                            } else {
                                SquareRenderer.drawLegalMoveDot(g2, x, y, squareSize);
                            }
                            break;
                        }
                    }
                }

                // 6. Piece
                Piece piece = board.getPieceAt(pos);
                PieceRenderer.draw(g2, piece, x, y, squareSize);

                // 7. Labels (rank on col 0, file on row 7 — or mirrored)
                if (drawCol == 0) {
                    SquareRenderer.drawLabel(g2, String.valueOf(8 - row),
                            x, y, squareSize, isLight);
                }
                if (drawRow == 7) {
                    SquareRenderer.drawLabel(g2, String.valueOf((char)('a' + col)),
                            x + squareSize - squareSize / 6 - 3,
                            y, squareSize, isLight);
                }
            }
        }
    }
}
