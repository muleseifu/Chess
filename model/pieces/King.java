package Chess.model.pieces;


public class King{
    int x,y;
    public Kight(int color,String id,String imagePath){
        super(color,id,imagePath);
    }
    @Override
     public List<Cell> move(Cell pos, Board board) {
        List<Cell> destination=new ArrayList<>();
        int[][] destination={
            {-1, -1}, {-1, 0}, {-1, +1},
            { 0, -1},           { 0, +1},
            {+1, -1}, {+1, 0}, {+1, +1}
        };
        for (int[] dir : destination) {
            int row = pos.getX() + dir[0];
            int col = pos.getY() + dir[1];
            if (row >= 0 && row < 8 && col >= 0 && col < 8){
                Cell target = board.getCell(row, col);
                if (target.isEmpty() || target.isEnemy(this.color)) {
                    if (!wouldBeInDanger(board, row, col)) {
                        destinations.add(target);
                    }
                }
            }
        }
        if (canCastleKingside(board)) {
            destinations.add(board.getCell(pos.getX(), pos.getY() + 2));
        }
        if (canCastleQueenside(board)) {
            destinations.add(board.getCell(pos.getX(), pos.getY() - 2));
        }
     return distinations;
     }
