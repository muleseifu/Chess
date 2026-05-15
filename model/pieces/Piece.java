package Chess.model.pieces;

public abstract  class Piece {
    protected int color;
    protected String id ;
    protected String imagePath;
    protected boolean hasMoved;
    protected boolean availability;
    public void SetId(String id){
        this.id = id;
    }
    public void SetColor(int color) {
        this.color = color;
    }
    public void SetPath(String imagePath) {
        this.imagePath = imagePath;
    }
    public void setAvailable(boolean availability) {
        this.availability = availability;
    }
     public void setMoved() {
        this.hasMoved = true;
    }
    public int getColor() {
        return color;
    }
    public String getId() {
        return id;
    }
    public String getID()
    {
        return id;
    }
    
    public String getPath() {
        return imagePath;
    }
    public boolean isAvailable() {
        return availability;
    }
    public boolean hasMoved() {
        return hasMoved;
    }
  public void test() {
        System.out.println("This is a test method in the Piece class.");
    }
   





    
}
