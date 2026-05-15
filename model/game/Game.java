package Chess.model.game;
import Chess.model.board.Board;
import Chess.model.board.Board;
import Chess.model.board.Cell;
import Chess.util.MoveHistory;

public class Game {
    private Board board;
    private int currentturn;
    private GameMode gameMode;
    private Bot bot;
    private MoveHistory history;
    private GameStatus status;
    public void startGame(GameMode mode){

    }
    public void handleCellClick(Cell cell){

    }
    public void makeMove(Cell from ,Cell to){
        board.movePiece(from,to);
        updateStatus();

    }
    public enum GameMode{
        PvP,
        PvE
    }
    public void switchTurn(){
        currentturn = 1 - currentturn;

    }
    public void updateStatus(){
        if(board.isCheckmate(currentturn)){
            status = GameStatus.CHECKMATE;
        }else if(board.isStalemate(currentturn)){
            status = GameStatus.STALEMATE;
        }else if(board.isInCheck(currentturn)){
            status = GameStatus.ONGOING; // maybe add a CHECK state later
        }else{
            status = GameStatus.ONGOING;
        }

        
    }
    public void undoMove(){
        
    }
    public void resignGame(int color){

        
        
        
    }
    public void offerDraw(){

    }
    public enum GameStatus{
        ONGOING,
        CHECKMATE,
        STALEMATE,
        DRAW,
        RESIGNATION

    }
    public int getCurrentTurn(){

    }
    public void restartGame(){
        
    }
}
