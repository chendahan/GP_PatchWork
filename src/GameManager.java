import io.jenetics.prog.ProgramGene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {
	private static int boardSize=50;
	private static int playerBoardSize=9;
	private List<Integer> buttonPositions;
	private List<Integer> specialPiecePositions;
	private List<Piece> piecesInCircle;
	private int pieceIndex; // use index%32 in get
	private Player opponent, ourPlayer;
	private int currentPlayer;

	public GameManager(List<Piece> pieces) {
		Collections.shuffle(pieces);
		this.piecesInCircle = pieces;
		buttonPositions=new ArrayList<>(Arrays.asList(5,10,15,20,24,31,36,42,47));
		specialPiecePositions=new ArrayList<>(Arrays.asList(10,15,22,30,38));
		opponent=new Player(1);
		ourPlayer=new Player(2);
		//currentPlayer=1;
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
		//System.out.println("pieceIndex: " + index);
		List<Piece> NextAvailablePieces = new ArrayList<>();
		for(int i=0;i<3;i++)
		{
			NextAvailablePieces.add(this.piecesInCircle.get((this.pieceIndex+i)%this.piecesInCircle.size()));
		}
		return NextAvailablePieces;
	}
	
	//0/1/2
	public void updateUsedPiece(int indxUsed)
	{
		this.piecesInCircle.remove((indxUsed+this.pieceIndex)%this.piecesInCircle.size());
		pieceIndex=(indxUsed+pieceIndex)%this.piecesInCircle.size();
	}
	
	public Player getNextPlayer() {
		 return opponent.getPosition() < ourPlayer.getPosition() ?
				 opponent : ourPlayer;
	}
	
	public boolean canSelectPiece(Piece piece, Player player) {
	    return piece.getCost() > player.getButtons() ? false : true;
	}

	public int countNewButtons(int newPosition, Player player) {
		int nextButtonIndex = player.getLastButtonIndex() + 1;
		// Skip if passed all buttons already
		if (nextButtonIndex < 9 && newPosition >= buttonPositions.get(nextButtonIndex)) {
			// collect buttons
			player.setLastButtonIndex(nextButtonIndex);
			return player.getButtons() + player.getPlayerBoard().getButtons();
		}
		return player.getButtons();
	}

	public int countEmptyCorners(PlayerBoard board) 
	{
		int count=0;
		count += board.getCell(this.playerBoardSize-1,0) ? 0: 1;
		count += board.getCell(0,this.playerBoardSize-1) ? 0: 1;
		count += board.getCell(0,0) ? 0: 1;
		count += board.getCell(this.playerBoardSize-1,this.playerBoardSize-1) ? 0: 1;
		
		return count;
	}


	public int countCoveredFrame(PlayerBoard board) 
	{
		int count=0;
		for (int i=0;i<this.playerBoardSize; i++)
		{
			count += board.getCell(i,0) ? 1: 0;
			count += board.getCell(0,i) ? 1: 0;
			count += board.getCell(i,this.playerBoardSize-1) ? 1: 0;
			count += board.getCell(this.playerBoardSize-1,i) ? 1: 0;
		}
		
		return count;
	}
	
	public int countFreeCells(PlayerBoard board) 
	{
		int count=0;
		for (int i=0;i<this.playerBoardSize; i++)
		{
			for (int j=0;j<this.playerBoardSize; j++)
			{
				count += board.getCell(i,j) ? 0: 1;
			}
		}
		
		return count;
	}
	
	public void placePiece(Player player, PieceAndCoord pieceAndCoord) {
//		if(!player.getPlayerBoard().placePiece(pieceShape, position))
//			return false;
		Piece piece = pieceAndCoord.piece;
		Dot position = pieceAndCoord.coord;
		List<Dot> pieceShape = pieceAndCoord.shape;
		player.getPlayerBoard().placePiece(piece, pieceShape, position);
		int newPosition = player.getPosition()+piece.getTime();
		player.setButtons(countNewButtons(newPosition, player)-piece.getButtons());
		player.setPosition(newPosition);
		updateUsedPiece(pieceAndCoord.index);
		//return true;
	}

	public void moveNoPiece(Player player, int position) {
		int prev_pos = player.getPosition();
		player.setPosition(position);
		int numStepsToMove = position - prev_pos;
		int newButtons = numStepsToMove + countNewButtons(position, player);
		player.setButtons(newButtons);
	}

	public boolean possiblePiecesPlacements(Player player) {
		
		
		return true;
	}
	

	// Used by the random player
	public void makeRandomMove()
	{
		int placePiece = (int) (Math.random() * 2);
		if (placePiece == 1) {
			List<Piece> pieces = getNextPiecesAvailableToSelect();
			PlayerBoard board = opponent.getPlayerBoard();
			List<PieceAndCoord> availablePieces = new ArrayList<>();
			int index = 0;
			for (Piece piece : pieces) {
				if (!canSelectPiece(piece, opponent)) // skip too expensive pieces
					continue;
				for (int i = 0; i < playerBoardSize; i++) {
					for (int j = 0; j < playerBoardSize; j++) {
						Dot dot = new Dot(i, j);
						List<List<Dot>> shapesList = Arrays.asList(piece.getShape(), piece.getShape_90(),
								piece.getShape_180(), piece.getShape_270());
						for (List<Dot> pieceShape : shapesList) {
							if (board.isLegalPlacement(pieceShape, dot)) {
								PieceAndCoord option = new PieceAndCoord(piece, dot, pieceShape, index);
								availablePieces.add(option);
							}
						}
					}
				}
				index++;
			}
			if (availablePieces.size() != 0) {
				int chosenAtIndex = (int) (Math.random() * availablePieces.size());
				PieceAndCoord chosenPiece = availablePieces.get(chosenAtIndex);
				placePiece(opponent, chosenPiece);
				return;
			}
		}
		// If there are no available pieces or placePiece is false
		moveNoPiece(opponent, ourPlayer.getPosition() + 1);

	}

	public void makeMove(final ProgramGene<Double> program)
	{
		List<Piece> pieces = getNextPiecesAvailableToSelect();
		int index = 0;
		PlayerBoard board = ourPlayer.getPlayerBoard();
		double max_res = -100000;
		boolean init_max_res = false;
		PieceAndCoord chosenPiece = new PieceAndCoord();
		boolean firstOption = true;
		Double[] terminals = new Double[6];
		// First option: place a piece
		for (Piece piece : pieces) {
			if (!canSelectPiece(piece, ourPlayer)) // skip too expensive pieces
				continue;
			for (int i = 0; i < playerBoardSize; i++) {
				for (int j = 0; j < playerBoardSize; j++) {
					Dot dot = new Dot(i, j);
					List<List<Dot>> shapesList = Arrays.asList(piece.getShape(), piece.getShape_90(),
							piece.getShape_180(), piece.getShape_270());
					for (List<Dot> pieceShape : shapesList) {
						if (board.isLegalPlacement(pieceShape, dot)) {
							PlayerBoard copy = new PlayerBoard(board); // copy board for simulation
							copy.placePiece(piece, pieceShape, dot); // simulate placement
							int new_pos = ourPlayer.getPosition() + piece.getTime();
							terminals[0] = (double)(new_pos); // new position
							terminals[1] = (double)(countNewButtons(new_pos, ourPlayer) -
											piece.getCost()); // new amount of buttons
							terminals[2]= (double)(countEmptyCorners(copy));
							terminals[3]= (double)(countCoveredFrame(copy));
							terminals[4]= (double)(pieceShape.size());
							terminals[5]= (double)(countFreeCells(copy));
							double res = program.apply(terminals);
							if (!init_max_res) {
								init_max_res = true;
								max_res = res;
								chosenPiece = new PieceAndCoord(piece, dot, pieceShape, index);
							} else if (res > max_res) {
								max_res = res;
								chosenPiece = new PieceAndCoord(piece, dot, pieceShape, index);
							}
						}
					}
				}
			}
			index++;
		}
		// Second option: just advance the player to get buttons (intuitively, this should be used only when
		// the player has no / very little buttons left
		int numStepsToMove = ourPlayer.getPosition() - opponent.getPosition() + 1;
		terminals[0] = (double) (opponent.getPosition() + 1); // new position
		terminals[1] = (double) (numStepsToMove +
				countNewButtons(opponent.getPosition() + 1, ourPlayer));// new amount of buttons
		terminals[2]= (double)(countEmptyCorners(board));
		terminals[3]= (double)(countCoveredFrame(board));
		terminals[4]= (double)(0);//pieceShape.size()- no shape 
		terminals[5]= (double)(countFreeCells(board));
		double res = program.apply(terminals);
		if (!init_max_res || res > max_res) { // could be if player has no buttons to buy more pieces
			firstOption = false;
		}
		if (firstOption)
			placePiece(ourPlayer, chosenPiece);
		else
			moveNoPiece(ourPlayer, opponent.getPosition() + 1);

	}

	public Results getResults(Player opponent, Player gpPlayer)
	{
		int opponentScore = 0;
		int gpScore = 0;
		if (opponent.hasSevenBySeven())
		{
			opponentScore += 7;
		} else if (gpPlayer.hasSevenBySeven())
		{
			gpScore += 7;
		}
		opponentScore += opponent.getButtons();
		gpScore += gpPlayer.getButtons();
		System.out.println("Opponent buttons: " + opponentScore);
		System.out.println("GP buttons: " + gpScore);
		opponentScore -= 2 * (opponent.getPlayerBoard().countEmptyCells());
		gpScore -= 2 * (gpPlayer.getPlayerBoard().countEmptyCells());
		System.out.println("Opponent score: " + opponentScore);
		System.out.println("GP score: " + gpScore);
		Results res = new Results();
		res.ourPlayerScore = gpScore;
		res.opponentScore = opponentScore;
		return res;
	}

	// TODO: add more statistics about the game to "Results" class
	public Results playGame(final ProgramGene<Double> program)
	{
		int first = (int) (Math.random() * 2);
		int i = 0;
		Player next = ourPlayer;
		// randomly choose first player
		if (i % 2 == first)
			next = opponent; // player1 is random player
		while(opponent.getPosition() < boardSize-1 || ourPlayer.getPosition() < boardSize-1) {
			// the player whose turn it is & didn't get to the finish plays
			if (next == opponent) {
				if (opponent.getPosition() == boardSize-1)
					continue;
				makeRandomMove();
			} else { // next == player2
				if (ourPlayer.getPosition() == boardSize-1)
					continue;
				makeMove(program);
			}
			i++;
			next = getNextPlayer();
		}
		return getResults(opponent, ourPlayer);
	}
}

