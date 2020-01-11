
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
	
	public void setRow(int _row) {
		this.row=_row;
	}
	
	public void setColumn(int _col) {
		this.column=_col;
	}

	  @Override
	  public boolean equals(Object v) {
	        boolean retVal = false;

	        if (v instanceof Dot){
	        	Dot dotV=(Dot)v;
	           if(dotV.getRow()==this.row && dotV.getColumn()==this.column)
	        	   retVal=true;
	        }

	     return retVal;
	  }
	  

}
