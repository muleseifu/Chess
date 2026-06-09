package chess.model.board;

import chess.model.Move;
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;
import chess.model.pieces.Piece;
import chess.model.pieces.PieceFactory;
import chess.model.state.GameState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Central game-state class. Holds:
 * <ul>
 *   <li>The 8×8 grid of pieces.</li>
 *   <li>Whose turn it is.</li>
 *   <li>Castling rights.</li>
 *   <li>The last move played (for en passant).</li>
 *   <li>The full move history.</li>
 *   <li>The current game state (playing / check / checkmate / stalemate).</li>
 * </ul>
 *
 * <p>Delegates physical move application to {@link MoveExecutor},
 * legality filtering to {@link MoveValidator}, and initial setup to
 * {@link BoardSetup}.</p>
 */
public class Board implements ChessBoard {

    private final Piece[][]      squares;
    private PieceColor           currentTurn;
    private Move                 lastMove;
    private final CastlingRights castlingRights;
    private GameState            gameState;
    private final List<Move>     moveHistory;

    // ─── Constructors ────────────────────────────────────────────────────────

    /** Create a board in the standard starting position. */
    public Board() {
        squares        = new Piece[8][8];
        currentTurn    = PieceColor.WHITE;
        castlingRights = new CastlingRights();
        gameState      = GameState.PLAYING;
        moveHistory    = new ArrayList<>();
        BoardSetup.placeInitialPieces(squares);
    }

    /** Deep-copy constructor (used by AI and move validation). */
    public Board(Board other) {
        this.squares = new Piece[8][8];
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                this.squares[r][c] = PieceFactory.copy(other.squares[r][c]);

        this.currentTurn    = other.currentTurn;
        this.lastMove       = other.lastMove;
        this.castlingRights = new CastlingRights(other.castlingRights);
        this.gameState      = other.gameState;
        this.moveHistory    = new ArrayList<>(other.moveHistory);
    }

    // ─── ChessBoard interface ────────────────────────────────────────────────

    @Override
    public Piece getPieceAt(Position pos) {
        if (!pos.isValid()) return null;
        return squares[pos.row][pos.col];
    }

    @Override
    public Move getLastMove() { return lastMove; }

    @Override
    public CastlingRights getCastlingRights() { return castlingRights; }

    @Override
    public boolean isSquareAttackedBy(Position pos, PieceColor byColor) {
        return MoveValidator.isSquareAttacked(pos, byColor, this);
    }

    // ─── Direct square access (for tests / setup) ────────────────────────────

    public void setPieceAt(Position pos, Piece piece) {
        squares[pos.row][pos.col] = piece;
    }

    // ─── Move generation ─────────────────────────────────────────────────────

    /** Returns all fully legal moves for the given color. */
    public List<Move> getLegalMoves(PieceColor color) {
        List<Move> all = new ArrayList<>();
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                if (squares[r][c] != null && squares[r][c].getColor() == color)
                    all.addAll(getLegalMovesForPiece(new Position(r, c)));
        return all;
    }

    /** Returns all fully legal moves for the piece at {@code pos}. */
    public List<Move> getLegalMovesForPiece(Position pos) {
        Piece piece = getPieceAt(pos);
        if (piece == null) return Collections.emptyList();
        List<Move> pseudo = piece.generatePseudoLegalMoves(pos, this);
        return MoveValidator.filterLegal(pseudo, this);
    }

    // ─── Move execution ──────────────────────────────────────────────────────

    /**
     * Attempt to make a move. Validates legality, applies it, updates
     * turn, game state, and history.
     *
     * @return true if the move was legal and applied; false otherwise
     */
    public boolean makeMove(Move move) {
        List<Move> legal = getLegalMovesForPiece(move.getFrom());
        Move matched = findMatch(legal, move);
        if (matched == null) return false;

        applyMoveInternal(matched);  // also flips currentTurn
        moveHistory.add(matched);
        lastMove = matched;
        // Note: do NOT flip currentTurn again here — applyMoveInternal already did it.
        updateGameState();
        return true;
    }

    /**
     * Apply a move directly, without legality checking.
     * Used internally and by the AI on board copies.
     */
    public void applyMoveInternal(Move move) {
        MoveExecutor.apply(squares, move, castlingRights);
        // Switch turn on copies too (needed for minimax)
        currentTurn = currentTurn.opposite();
    }

    // ─── Check / game-state detection ────────────────────────────────────────

    public boolean isInCheck(PieceColor color) {
        Position kingPos = findKing(color);
        if (kingPos == null) return false;
        return MoveValidator.isSquareAttacked(kingPos, color.opposite(), this);
    }

    private void updateGameState() {
        List<Move> moves = getLegalMoves(currentTurn);
        if (moves.isEmpty()) {
            gameState = isInCheck(currentTurn) ? GameState.CHECKMATE : GameState.STALEMATE;
        } else if (isInCheck(currentTurn)) {
            gameState = GameState.CHECK;
        } else {
            gameState = GameState.PLAYING;
        }
    }

    public Position findKing(PieceColor color) {
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++) {
                Piece p = squares[r][c];
                if (p != null && p.getType() == PieceType.KING && p.getColor() == color)
                    return new Position(r, c);
            }
        return null;
    }

    // ─── Board evaluation (for AI) ───────────────────────────────────────────

    /**
     * Static evaluation of the board from White's perspective.
     * Positive = White advantage; negative = Black advantage.
     */
    public int evaluate() {
        int score = 0;
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++) {
                Piece p = squares[r][c];
                if (p == null) continue;
                int val = p.getMaterialValue() + PositionTables.getBonus(p, r, c);
                score += p.isWhite() ? val : -val;
            }
        return score;
    }

    // ─── Accessors ───────────────────────────────────────────────────────────

    public PieceColor  getCurrentTurn() { return currentTurn; }
    public GameState   getGameState()   { return gameState; }
    public List<Move>  getMoveHistory() { return Collections.unmodifiableList(moveHistory); }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private Move findMatch(List<Move> legal, Move requested) {
        for (Move m : legal) {
            if (m.getTo().equals(requested.getTo())
                    && m.getFrom().equals(requested.getFrom())
                    && m.getPromotionType() == requested.getPromotionType()) {
                return m;
            }
        }
        return null;
    }
}
