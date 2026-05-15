package Chess.model.pieces;

import java.util.List;
import Chess.model.board.Cell;
import Chess.model.board.Board;

public abstract class Piece {

	protected int color; // 0 = White, 1 = Black
	protected String id; // unique piece ID
	protected String imagePath; // path to PNG sprite
	protected boolean availability; // false when captured
	protected boolean hasMoved; // used for castling / en passant

	public Piece() {
		this.id = "";
		this.color = 0;
		this.imagePath = "";
		this.availability = true;
		this.hasMoved = false;
	}

	public Piece(String id, int color, String imagePath) {
		this.id = id;
		this.color = color;
		this.imagePath = imagePath;
		this.availability = true;
		this.hasMoved = false;
	}

	public void setId(String id){
		this.id = id;
	}

	public void setPath(String path){
		this.imagePath = path;
	}

	public void setColor(int color){
		this.color = color;
	}

	public String getId(){
		return this.id;
	}

	public String getPath(){
		return this.imagePath;
	}

	public int getColor(){
		return this.color;
	}

	public boolean isAvailable(){
		return this.availability;
	}

	public void setAvailable(boolean b){
		this.availability = b;
	}

	public boolean hasMoved(){
		return this.hasMoved;
	}

	/**
	 * Mark this piece as having moved (used for castling/en-passant logic).
	 */
	public void setMoved(){
		this.hasMoved = true;
	}

	/**
	 * Return a deep copy of this piece. Concrete subclasses must implement.
	 */
	public abstract Piece getCopy();

	/**
	 * Return a list of legal destination Cells for this piece from the given
	 * current cell on the provided board.
	 */
	public abstract List<Cell> move(Cell curr, Board b);

	@Override
	public String toString(){
		return id + "(color=" + color + ", available=" + availability + ", hasMoved=" + hasMoved + ")";
	}

}
