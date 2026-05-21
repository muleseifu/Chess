package Chess.model.pieces;

import Chess.model.board.Board;
import Chess.model.board.Cell;
import java.util.List;
import java.util.ArrayList;
public class Bishop extends Piece {
    public Bishop(int color, String id, String imagePath) {
        super(color, id, imagePath);
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

    