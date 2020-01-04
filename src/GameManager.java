import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {
	
	private static int boardSize=50;
	private List<Integer> buttonPositions;
	private List<Integer> specialPiecePositions;
	private List<Piece> piecesInCircle;
	private int pieceIndex; // use index%32 in get
	private Player player1,player2;
	private int currentPlayer;
	
	public GameManager(List<Piece> pieces) {
		Collections.shuffle(pieces);
		this.piecesInCircle = pieces;
		buttonPositions=new ArrayList<>(Arrays.asList(5,10,15,20,24,31,36,42,47));
		specialPiecePositions=new ArrayList<>(Arrays.asList(10,15,22,30,38));
		player1=new Player(1);
		player2=new Player(2);		
		currentPlayer=1;
		pieceIndex=findStartedPiece();//
	}
	
	public int findStartedPiece() {
		int index = 0;
		for(Piece piece : piecesInCircle) {
			if(piece.getId() == 0)
				return index;
		}
		return -1;
	}
	
	public List<Piece> getNextPiecesAvailableToSelect(){
		int counter = 0;
		int index = pieceIndex;
		List<Piece> NextAvailablePieces = new ArrayList<>();
		while(counter!=3) {
			if(!piecesInCircle.get(index).isUsed()) {
				NextAvailablePieces.add(piecesInCircle.get(index));
				counter++;
			}
			index = index == 31 ? 0 : index+1;
		}
		return NextAvailablePieces;
	}
	
	public Player getNextPlayer() {
		 return player1.getPosition() < player2.getPosition() ? player1 : player2;
	}
	
	public boolean canSelectPiece(Piece piece, Player player) {
	    return piece.getCost() > player.getButtons() ? false : true;
	}
	
	public boolean placePiece(Player player, Piece piece, List<Dot> pieceShape, Dot position) {
		if(!player.getPlayerBoard().placePiece(pieceShape, position))
			return false;
		player.setButtons(player.getButtons()-piece.getButtons());
		player.setPosition(player.getPosition()+piece.getTime());
		//TODO: mark piece unav
		return true;
	}
	
	public boolean possiblePiecesPlacements(Player player) {
		
		
		return true;
	}
	
	
	public void MakeAmove()
	{
		//1- just move
		
		//2- place a piece on board
	}
}
