package Chess.model.pieces;

import Chess.model.board.Board;
import Chess.model.board.Cell;
import java.util.List;
import java.util.ArrayList;
public class Knight extends Piece{
    public Knight(String id, int color, String imagePath) {
        super(id, color, imagePath);
    }
    @override
    public List<Cell> move(Cell pos,Board board){
    return;
    }
    
    @override
    public Piece getCopy(){
    return;
    }
}