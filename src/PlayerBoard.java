import java.util.ArrayList;
import java.util.List;

public class PlayerBoard {
	private SquarePlayerBoard[][] squarePlayerBoards;
	static int boardSize=9;
	int numOfButtons;
	
	public PlayerBoard(Player player) {
		this.squarePlayerBoards = new SquarePlayerBoard[boardSize][boardSize];
		this.numOfButtons=0;
	}
	
	
	
}


