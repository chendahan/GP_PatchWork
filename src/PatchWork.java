import java.util.Arrays;
import java.util.List;

public class PatchWork {

	public static void main(String[] args) {
		PieceGenerator gen = new PieceGenerator();
		List<Piece> pieces = gen.getClassicPieces();
		GameManager boardGame=new GameManager(pieces);
		
		for(int i=0;i<pieces.size();i++) {
			String[][] board=new String[5][5];
			for (int x=0;x<5;x++)
			{
				for (int y=0;y<5;y++)
				{
					board[x][y]="-";
				}
			}
			Piece p = pieces.get(i);
			System.out.println("shape id:"+ p.getId());
			for(Dot sq : p.getShape()) {
				board[sq.getRow()][sq.getColumn()]="+";
			}
			for (int x=0;x<5;x++)
			{
				for (int y=0;y<5;y++)
				{
					System.out.print(board[x][y]);
				}
				System.out.println();
			}
			
			if(p.getShape_90() != null)
			{
				for (int x=0;x<5;x++)
				{
					for (int y=0;y<5;y++)
					{
						board[x][y]="-";
					}
				}
				System.out.println("shape 90:"+ p.getId());
				for(Dot sq : p.getShape_90()) {
					board[sq.getRow()][sq.getColumn()]="+";
				}
				for (int x=0;x<5;x++)
				{
					for (int y=0;y<5;y++)
					{
						System.out.print(board[x][y]);
					}
					System.out.println();
				}
			}
			if(p.getShape_180() != null)
			{
				for (int x=0;x<5;x++)
				{
					for (int y=0;y<5;y++)
					{
						board[x][y]="-";
					}
				}
				System.out.println("shape 180:"+ p.getId());
				for(Dot sq : p.getShape_180()) {
					board[sq.getRow()][sq.getColumn()]="+";
				}
				for (int x=0;x<5;x++)
				{
					for (int y=0;y<5;y++)
					{
						System.out.print(board[x][y]);
					}
					System.out.println();
				}
			}
			if(p.getShape_270() != null)
			{
				for (int x=0;x<5;x++)
				{
					for (int y=0;y<5;y++)
					{
						board[x][y]="-";
					}
				}
				System.out.println("shape 270:"+ p.getId());
				for(Dot sq : p.getShape_270()) {
					board[sq.getRow()][sq.getColumn()]="+";
				}
				for (int x=0;x<5;x++)
				{
					for (int y=0;y<5;y++)
					{
						System.out.print(board[x][y]);
					}
					System.out.println();
				}
			}
		}
				
	}

}
