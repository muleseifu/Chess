package Chess.model.board;
import javax.swing.*;
import Chess.model.pieces.Piece; 
public class Cell {
    private boolean isPossibleDestination;
    private JLabel content;
    private Piece piece;
    private boolean isSelected;
    private boolean isCheck;
    private int x,y;
    //private Color baseColor;
    public void setPiece( Piece piece){
        this.piece = piece;
        updateSprite();

    }
     public void select(){
        this.isSelected = true;

    }
     public void deselect(){
        this.isSelected = false;

    }
     public boolean  isSelected(){
        return isSelected;

    }
     public void removePiece(){
        this.piece = null;

    }
    public Piece getPiece(){
        return piece;
    }
    public void setPossibleDestination(){
        this.isPossibleDestination = true;
        
    }
    public void removePossibleDestination(){
        this.isPossibleDestination = false;
        
    }
    public boolean isPossibleDestination(){
        return isPossibleDestination;
    }
    public void setCheck(){
        this.isCheck = true;
        
    }
    public void removeCheck(){
        this.isCheck = false;
    }
    public boolean isCheck(){
        return isCheck;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public boolean isEmpty(){
        return piece == null;
    }
    public boolean isEnemy(int color){
        if(piece == null) return false;
        return piece.getColor() != color;
        

    }
    public void updateSprite(){
        
        

    }
    


}