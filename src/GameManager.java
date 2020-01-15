import io.jenetics.prog.ProgramGene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {
    private static int boardSize = 50;
    private static int playerBoardSize = 9;
    private List<Integer> buttonPositions;
    private List<Integer> specialPiecePositions;
    private List<Piece> piecesInCircle;
    private int pieceIndex; // use index%32 in get
    private Player opponent, ourPlayer;
    private int currentPlayer;

    public GameManager(List<Piece> pieces) {
        Collections.shuffle(pieces);
        this.piecesInCircle = pieces;
        buttonPositions = new ArrayList<>(Arrays.asList(5, 10, 15, 20, 24, 31, 36, 42, 47));
        specialPiecePositions = new ArrayList<>(Arrays.asList(10, 15, 22, 30, 38));
        opponent = new Player(1);
        ourPlayer = new Player(2);
        //currentPlayer=1;
        pieceIndex = findStartedPiece();//
    }

    public int findStartedPiece() {
        int index = 0;
        for (Piece piece : piecesInCircle) {
            if (piece.getId() == 0)
                return index;
        }
        return -1;
    }

    public List<Piece> getNextPiecesAvailableToSelect() {
        //System.out.println("pieceIndex: " + index);
        List<Piece> NextAvailablePieces = new ArrayList<>();
        int min = Math.min(3, piecesInCircle.size());
        for (int i = 0; i < min; i++) {
            NextAvailablePieces.add(this.piecesInCircle.get((this.pieceIndex + i) % this.piecesInCircle.size()));
        }
        return NextAvailablePieces;
    }

    //0/1/2
    public void updateUsedPiece(int indxUsed) {
        this.piecesInCircle.remove(indxUsed);
        if (this.piecesInCircle.size() == 0)
            pieceIndex = 0;
        else
            pieceIndex = indxUsed % this.piecesInCircle.size();
    }

    public Player getNextPlayer() {
        return opponent.getPosition() < ourPlayer.getPosition() ?
                opponent : ourPlayer;
    }

    public boolean canSelectPiece(Piece piece, Player player) {
        return piece.getCost() > player.getButtons() ? false : true;
    }

    // set parameter is used to differentiate simulation from actual game move
    public int countNewButtons(int newPosition, Player player, boolean set) {
        int nextButtonIndex = player.getLastButtonIndex() + 1;
        // Skip if passed all buttons already
        if (nextButtonIndex < 9 && newPosition >= buttonPositions.get(nextButtonIndex)) {
            // collect buttons
            if (set)
                player.setLastButtonIndex(nextButtonIndex);
            return player.getButtons() + player.getPlayerBoard().getButtons();
        }
        return player.getButtons();
    }

    public int countEmptyCorners(PlayerBoard board) {
        int count = 0;
        count += board.getCell(this.playerBoardSize - 1, 0) ? 0 : 1;
        count += board.getCell(0, this.playerBoardSize - 1) ? 0 : 1;
        count += board.getCell(0, 0) ? 0 : 1;
        count += board.getCell(this.playerBoardSize - 1, this.playerBoardSize - 1) ? 0 : 1;

        return count;
    }


    public int countCoveredFrame(PlayerBoard board) {
        int count = 0;
        for (int i = 0; i < this.playerBoardSize; i++) {
            count += board.getCell(i, 0) ? 1 : 0;
            count += board.getCell(0, i) ? 1 : 0;
            count += board.getCell(i, this.playerBoardSize - 1) ? 1 : 0;
            count += board.getCell(this.playerBoardSize - 1, i) ? 1 : 0;
        }
        
        if(board.getCell(0, 0))
        	count-=1;

        if(board.getCell(0, 8))
        	count-=1;

        if(board.getCell(8, 0))
        	count-=1;

        if(board.getCell(8, 8))
        	count-=1;

        return count;
    }

    // counts how many cells are empty with full cells around them
    public int countEnclosedCells(PlayerBoard board) {
        int count = 0;
        for (int i=0;i< playerBoardSize; i++) {
            for (int j = 0; j < playerBoardSize; j++) {
                if (i==0 && j==0) {
                    if (board.getCell(0, 1) == true && board.getCell(1, 0) == true)
                        count++;
                }
                else if (i==playerBoardSize-1 && j==0) {
                    if (board.getCell(playerBoardSize-1, 1) == true && board.getCell(playerBoardSize-2, 0) == true)
                        count++;
                }
                else if (i==playerBoardSize-1 && j==playerBoardSize-1) {
                    if (board.getCell(playerBoardSize-1, playerBoardSize-2) == true &&
                            board.getCell(playerBoardSize-2, playerBoardSize-1) == true)
                        count++;
                }
                else if (i==0 && j==playerBoardSize-1) {
                    if (board.getCell(1, playerBoardSize-1) == true &&
                            board.getCell(0, playerBoardSize-2) == true)
                        count++;
                }
                else if (i==0)
                {
                    if (board.getCell(0, j-1) == true && board.getCell(0, j+1) == true &&
                            board.getCell(1, j) == true)
                        count++;
                }
                else if (i==playerBoardSize-1)
                {
                    if (board.getCell(playerBoardSize-1, j-1) == true &&
                            board.getCell(playerBoardSize-1, j+1) == true &&
                            board.getCell(playerBoardSize-2, j) == true)
                        count++;
                }
                else if (j==0)
                {
                    if (board.getCell(i-1, 0) == true && board.getCell(i+1, 0) == true &&
                            board.getCell(i, 1) == true)
                        count++;
                }
                else if (j==playerBoardSize-1)
                {
                    if (board.getCell(i-1, playerBoardSize-1) == true &&
                            board.getCell(i+1,playerBoardSize-1) == true &&
                            board.getCell(i, playerBoardSize-2) == true)
                        count++;
                }
                else // inner cell
                {
                    if (board.getCell(i, j-1) == true && board.getCell(i, j+1) == true &&
                            board.getCell(i-1, j) == true && board.getCell(i+1, j) == true)
                        count++;
                }
            }
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
		int newPosition = Math.min(boardSize-1, player.getPosition()+piece.getTime());
		player.setButtons(countNewButtons(newPosition, player, true)-piece.getCost());
		player.setPosition(newPosition);
		updateUsedPiece(pieceAndCoord.index);
		//return true;
	}

	public void moveNoPiece(Player player, int position) {
		int prev_pos = player.getPosition();
		player.setPosition(position);
		int numStepsToMove = position - prev_pos;
		int newButtons = numStepsToMove + countNewButtons(position, player, true);
		player.setButtons(newButtons);
	}

	public boolean possiblePiecesPlacements(Player player) {
		
		
		return true;
	}
	

	// Used by the random player
	public void makeRandomMove()
	{
		int placePiece;
		int numStepsToMove = ourPlayer.getPosition() - opponent.getPosition() + 1;
		if (numStepsToMove < 1) {
			placePiece = 1; // if opponent is already ahead of the other player, can only place a new piece
		}
		else // if both options are available, choose randomly which option to take
			placePiece = (int) (Math.random() * 2);
		if (placePiece == 1) {
			List<Piece> pieces = getNextPiecesAvailableToSelect();
			PlayerBoard board = opponent.getPlayerBoard();
			List<PieceAndCoord> availablePieces = new ArrayList<>();
			int index = pieceIndex;
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
				index = (index+1)%this.piecesInCircle.size();
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

	public void makeMove(final ProgramGene<Double> program, Player player, Player secondPlayer)
	{
		List<Piece> pieces = getNextPiecesAvailableToSelect();
		int index = pieceIndex;
		PlayerBoard board = player.getPlayerBoard();
		double max_res = -100000;
		boolean init_max_res = false;
		PieceAndCoord chosenPiece = new PieceAndCoord();
		boolean firstOption = false;
		Double[] terminals = new Double[12];
		// First option: place a piece
		for (Piece piece : pieces) {
			if (!canSelectPiece(piece, player)) // skip too expensive pieces
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
							int new_pos = Math.min(boardSize-1, player.getPosition() + piece.getTime());
							terminals[0] = (double)(new_pos); // new position
							terminals[1] = (double)(countNewButtons(new_pos, player, false) -
											piece.getCost()); // new amount of buttons
							terminals[2]= (double)(countEmptyCorners(copy));
							terminals[3]= (double)(countCoveredFrame(copy));
							terminals[4]= (double)(pieceShape.size());
							terminals[5]= (double)(countFreeCells(copy));
							terminals[6]= (double)(countEmptySurrounding(board,pieceShape,dot));
							terminals[7] = (double)(countEnclosedCells(copy));
							terminals[8] = (double)(piece.getButtons());
							terminals[9] = (double)(piece.getCost());
							terminals[10] = (double)(player.getLastButtonIndex());
							terminals[11] = (double)(opponent.getPosition());
							double res = program.apply(terminals);
							if (!init_max_res) {
								init_max_res = true;
								max_res = res;
								chosenPiece = new PieceAndCoord(piece, dot, pieceShape, index);
								firstOption = true;
							} else if (res > max_res) {
								max_res = res;
								chosenPiece = new PieceAndCoord(piece, dot, pieceShape, index);
								firstOption = true;
							}
						}
					}
				}
			}
			index = (index+1)%this.piecesInCircle.size();
		}
		// Second option: just advance the player to get buttons
		int new_pos = Math.min(boardSize-1, secondPlayer.getPosition()+1);
		int numStepsToMove = new_pos - player.getPosition();
		if (numStepsToMove > 0) { // SHOULD ALWAYS BE TRUE - it's always the turn of the player who is behind
			terminals[0] = (double) (new_pos);
			terminals[1] = (double) (numStepsToMove +
					countNewButtons(new_pos, player, false));// new amount of buttons
			terminals[2]= (double)(countEmptyCorners(board));
			terminals[3]= (double)(countCoveredFrame(board));
			terminals[4]= (double)(0);//pieceShape.size()- no shape 
			terminals[5]= (double)(countFreeCells(board));
			terminals[6]= (double)0;//no piece surroundings
            terminals[7] = (double)(countEnclosedCells(board));
			terminals[8] = (double)(0);
			terminals[9] = (double)(0);
			terminals[10] = (double)(player.getLastButtonIndex());
			terminals[11] = (double)(opponent.getPosition());
			double res = program.apply(terminals);
			if (!init_max_res || res > max_res) { // could be if player has no buttons to buy more pieces
				firstOption = false;
			}
		}

		if (firstOption)
		{
			placePiece(player, chosenPiece);
		}
		else
			moveNoPiece(player, new_pos);
	}

	private double countEmptySurrounding(PlayerBoard board, List<Dot> pieceShape,Dot coord)
	{
		int count=0;
		List<Dot> pieceShapCopy=new ArrayList<Dot>();
		for(Dot dot:pieceShape)
			pieceShapCopy.add(new Dot(dot.getRow(),dot.getColumn()));
			
		//check up- x+1 for all dots
		for(Dot dot:pieceShapCopy)
		{
			dot.setColumn(dot.getColumn()+coord.getColumn());
			dot.setRow(dot.getRow()+1 +coord.getRow());
			if ((dot.getRow()<this.playerBoardSize) && !pieceShape.contains(dot))
			{
				count+= board.getCell(dot.getRow(),dot.getColumn())? 0:1;
			}
		}		
		//check down- x-1 for all dots
		for(Dot dot:pieceShapCopy)
		{
			dot.setRow(dot.getRow()-2);
			if ((dot.getRow()>0) &&!pieceShape.contains(dot))
			{
				count+= board.getCell(dot.getRow(),dot.getColumn())? 0:1;
			}
			dot.setRow(dot.getRow()+1);
		}
		
		//check right- y+1 for all dots
		for(Dot dot:pieceShapCopy)
		{
			dot.setColumn(dot.getColumn()+1);
			if ((dot.getColumn()<this.playerBoardSize) && !pieceShape.contains(dot))
			{
				count+= board.getCell(dot.getRow(),dot.getColumn())? 0:1;
			}
		}
		//check left- y-1 for all dots
		for(Dot dot:pieceShapCopy)
		{
			dot.setColumn(dot.getColumn()-2);
			if ((dot.getColumn()>0) && !pieceShape.contains(dot))
			{
				count+= board.getCell(dot.getRow(),dot.getColumn())? 0:1;
			}
		}
		
		return count;
	}

	public Results getResults(Player opponent, Player gpPlayer)
	{
		int opponentScore = 0;
		int gpScore = 0;
//		System.out.println("Opponent:");
//		for (int x=0;x<playerBoardSize;x++)
//		{
//			for (int y=0;y<playerBoardSize;y++)
//			{
//				if (opponent.getPlayerBoard().getCell(x ,y))
//					System.out.print("1");
//				else
//					System.out.print("0");
//			}
//			System.out.println();
//		}
//		System.out.println("GP:");
//		for (int x=0;x<playerBoardSize;x++)
//		{
//			for (int y=0;y<playerBoardSize;y++)
//			{
//				if (gpPlayer.getPlayerBoard().getCell(x ,y))
//					System.out.print("1");
//				else
//					System.out.print("0");
//			}
//			System.out.println();
//		}

		int opponentButtons = opponent.getButtons();
		int gpButtons = gpPlayer.getButtons();
		int opponentEmptyCells = opponent.getPlayerBoard().countEmptyCells();
		int gpEmptyCells = gpPlayer.getPlayerBoard().countEmptyCells();
		gpScore = gpButtons - 2 * gpEmptyCells;
		opponentScore = opponentButtons - 2 * opponentEmptyCells;
//		System.out.println("Opponent buttons: " + opponentButtons);
//		System.out.println("GP buttons: " + gpButtons);
//        System.out.println("Opponent empty cells: " + opponentEmptyCells);
//        System.out.println("GP empty cells: " + gpEmptyCells);
//		System.out.println("Opponent score: " + opponentScore);
//		System.out.println("GP score: " + gpScore);
		Results res = new Results();
		if (gpScore > opponentScore)
			res.winScore = 2;
		else if (gpScore == opponentScore)
			res.winScore = 1;
		else
			res.winScore = 0;
		res.opponentButtons = opponentButtons;
		res.ourPlayerButtons = gpButtons;
		res.opponentFilledCells = playerBoardSize*playerBoardSize - opponentEmptyCells;
		res.ourPlayerFilledCells = playerBoardSize*playerBoardSize - gpEmptyCells;
		return res;
	}


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
				makeMove(program, ourPlayer, opponent);
			}
			next = getNextPlayer();
		}
		return getResults(opponent, ourPlayer);
	}

	public Results playGame(final ProgramGene<Double> program, final ProgramGene<Double> programOpponent)
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
				makeMove(programOpponent, opponent, ourPlayer);
			} else { // next == player2
				if (ourPlayer.getPosition() == boardSize-1)
					continue;
				makeMove(program, ourPlayer, opponent);
			}
			next = getNextPlayer();
		}
		return getResults(opponent, ourPlayer);
	}
}

