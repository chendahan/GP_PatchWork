
public class Dot {
	int row,column;

	// Uninitialized dot
	public Dot()
	{
		row = -1;
		column = -1;
	}
	
	public Dot(int row, int column)
	{
		this.row=row;
		this.column = column;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getColumn() {
		return this.column;
	}

}
