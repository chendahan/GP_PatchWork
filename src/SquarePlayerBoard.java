
public class SquarePlayerBoard {
	
	private int x;
	private int y;
	private boolean filled;

//	Color color;
//	bool topStitching = false;
//	bool leftStitching = false;
	public SquarePlayerBoard(boolean filled) {
		  this.filled = filled;
//		  this.color = color;
		}
	
	public SquarePlayerBoard(int x, int y) {
		  this.x = x;
		  this.y = y;
		  this.filled = false;
	}
	
	public void setFilled(boolean isFilled) {
		this.filled = isFilled;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public boolean samePositionAs(SquarePlayerBoard squarePlayerBoard) {
		return squarePlayerBoard.x == this.x && squarePlayerBoard.y == this.y;
	}
}
