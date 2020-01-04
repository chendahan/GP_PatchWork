import java.util.ArrayList;
import java.util.List;

public class PlayerBoard {
	private SquarePlayerBoard[][] squarePlayerBoards;
	private boolean [][] playerBoard;
	static int boardSize=9;
	int numOfButtons;
	
	public PlayerBoard(Player player) {
		this.playerBoard = new boolean[boardSize][boardSize];
		this.numOfButtons=0;
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
	
	public boolean placePiece(List<Dot> pieceShape, Dot position) {
		if(!isLegalPlacement(pieceShape,position))
			return false;
		for(Dot dot : pieceShape)
			playerBoard[position.getRow() + dot.getRow()][position.getColumn() + dot.getColumn()] = true;
		return true;
	}
}


