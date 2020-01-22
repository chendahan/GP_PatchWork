import java.util.ArrayList;
import java.util.List;

public class PlayerBoard {
	private SquarePlayerBoard[][] squarePlayerBoards;
	private boolean [][] playerBoard;
	static int boardSize=9;

	private int numOfButtons;

	public boolean getCell(int i, int j) { return playerBoard[i][j]; }

	public int getButtons() { return numOfButtons; }
	
	public PlayerBoard(Player player) {
		this.playerBoard = new boolean[boardSize][boardSize];
		this.numOfButtons=0;
	}

	public PlayerBoard(PlayerBoard board) {
		boolean[][] boardCopy = new boolean[boardSize][];
		for(int k = 0; k < boardSize; k++)
			boardCopy[k] = board.playerBoard[k].clone();
		playerBoard = boardCopy;
		numOfButtons = board.numOfButtons;
	}
	
	public boolean isLegalPlacement(List<Dot> pieceShape, Dot position) {
		try {
			for(Dot dot : pieceShape) {
				if(playerBoard[position.getRow() + dot.getRow()][position.getColumn() + dot.getColumn()])
					return false;
			}
		}
		catch(ArrayIndexOutOfBoundsException exception) {
		    return false;
		}
		return true;
	}
	
	public void placePiece(Piece piece, List<Dot> pieceShape, Dot position) {
//		if(!isLegalPlacement(pieceShape,position))
//			return false;
		for(Dot dot : pieceShape)
			playerBoard[position.getRow() + dot.getRow()][position.getColumn() + dot.getColumn()] = true;
		// update buttons
		numOfButtons += piece.getButtons();
		//return true;
	}

	public int countEmptyCells() {
		int count = 0;
		for (int i=0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (playerBoard[i][j] == false)
				{
					count++;
				}
			}
		}
		return count;
	}

	public void print()
	{
		for(int i=0; i < this.boardSize; i++)
		{
			for(int j=0; j < this.boardSize; j++)
			{
				if(this.playerBoard[i][j])
					System.out.print("+");
				else
					System.out.print("-");
			}
			System.out.println();
		}
	}
}


