package Chess.model.pieces;

import Chess.model.board.Board;
import Chess.model.board.Cell;
import java.util.List;
import java.util.ArrayList;
public class Pawn extends Piece{
    boolean enPassantVulnerable = false;
    public Pawn(String id, int color, String imagePath){
        super(id,color,imagePath);
    }

    @override
    public List<Cell> move(Cell pos,Board board){
        return;
    }
    public Piece promote(String choice, Board board){
        return;
    }
    public boolean canEnPassant(Cell target){
        return;
    }
    @override
    public Piece getCopy(){

    }
}