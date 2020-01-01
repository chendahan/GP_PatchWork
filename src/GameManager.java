import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {
	
	private int positionP1,positionP2;
	private static int boardSize=50;
	List<Integer> buttonPositions;
	List<Integer> specialPiecePositions;
	List<Piece> piecesInCircle;
	int pieceIndex; // use index%32 in get
	Player player1,player2;
	int currentPlayer;
	
	public GameManager(List<Piece> pieces) {
		this.piecesInCircle= pieces;
		positionP1=positionP2=0;
		buttonPositions=new ArrayList<>(Arrays.asList(5,10,15,20,24,31,36,42,47));
		specialPiecePositions=new ArrayList<>(Arrays.asList(10,15,22,30,38));
		player1=new Player(1);
		player2=new Player(2);		
		currentPlayer=1;
		pieceIndex=0;
	}
	
	
	public void MakeAmove()
	{
		//1- just move
		
		//2- place a piece on board
	}
}
