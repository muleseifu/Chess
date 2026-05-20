package chess.model.pieces;

import chess.model.board.Board;
import chess.model.board.Cell;
import java.util.List;
import java.util.ArrayList;
public class Pawn extends Piece{
    boolean enPassantVulnerable = false;
    public Pawn(int color,String id,String imagePath){
        super(color,id,imagePath);
    }
    @override
    public List<Cell> move(Cell pos,Board board){
        return;
    }
    public Piece promote(String choice;Board board){
        return;
    }
    public boolean canEnPassant(Cell target){
        return;
    }
    @override
    public Piece getCopy(){

    }
}