package Chess.model.pieces;

import java.util.ArrayList;
import java.util.List;

import Chess.model.board.Board;
import Chess.model.board.Cell;

public class Queen extends Piece{
        public Queen(String id, int color, String imagePath){
            super(id, color, imagePath);
        }

        @Override
        public Piece getCopy(){
            return new Piece();

        }

        @Override
        public List<Cell> move(Cell pos, Board board){
            return new ArrayList<>();

         }

        






        


    
}
