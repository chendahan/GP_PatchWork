import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PieceGenerator {

	private List<Piece> classicPieces = new ArrayList<>();
	
	public PieceGenerator() 
	{	
		//first piece-smallest
		Piece p = new Piece(0, 0, 2, 1,Arrays.asList(0,0,0,1));
		p.setShape_90(Arrays.asList(0,0,1,0));
		p.setShape_180(Arrays.asList(0,0,0,1));
		p.setShape_270(Arrays.asList(0,0,1,0));
		classicPieces.add(p);
		
		//id, buttons on top, cost, time,List dots
		p=new Piece(1, 2, 7, 4,Arrays.asList(0,0,0,1,0,2,0,3,1,1,1,2));
		p.setShape_90(Arrays.asList(0,1,1,0,1,1,2,0,2,1,3,1));
		p.setShape_180(Arrays.asList(0,1,0,2,1,0,1,1,1,2,1,3));
		p.setShape_270(Arrays.asList(0,0,1,0,1,1,2,0,2,1,3,0));
		classicPieces.add(p);//x,y,x,y..
		
		p=new Piece(2, 0, 2, 1,Arrays.asList(0,1,1,0,1,1,1,2,1,3,2,2));
		p.setShape_90(Arrays.asList(0,1,1,1,1,2,2,0,2,1,3,1));
		p.setShape_180(Arrays.asList(0,1,1,0,1,1,1,2,1,3,2,2));
		p.setShape_270(Arrays.asList(0,1,1,1,1,2,2,0,2,1,3,1));
		classicPieces.add(p);

		p=new Piece(3, 1, 5, 3,Arrays.asList(0,1,0,2,1,0,1,1,1,2,1,3,2,1,2,2));
		p.setShape_90(Arrays.asList(0,1,1,0,1,1,1,2,2,0,2,1,2,1,2,2,3,1));
		p.setShape_180(Arrays.asList(0,1,0,2,1,0,1,1,1,2,1,3,2,1,2,2));
		p.setShape_270(Arrays.asList(0,1,1,0,1,1,1,2,2,0,2,1,2,2,3,1));
		classicPieces.add(p);
		
		p=new Piece(4, 2, 3, 6,Arrays.asList(0,0,0,2,1,0,1,1,1,2,2,1));
		p.setShape_90(Arrays.asList(0,1,0,2,1,0,1,1,2,1,2,2));
		p.setShape_180(Arrays.asList(0,1,1,0,1,1,1,2,2,0,2,2));
		p.setShape_270(Arrays.asList(0,0,0,1,1,1,1,2,2,0,2,1));
		classicPieces.add(p);
		
		p=new Piece(5, 2, 6, 5,Arrays.asList(0,0,0,1,1,0,1,1));
		p.setShape_90(Arrays.asList(0,0,0,1,1,0,1,1));
		p.setShape_180(Arrays.asList(0,0,0,1,1,0,1,1));
		p.setShape_270(Arrays.asList(0,0,0,1,1,0,1,1));
		classicPieces.add(p);
		
		p=new Piece(6, 1, 3, 3,Arrays.asList(0,0,0,1,0,2,0,3));
		p.setShape_90(Arrays.asList(0,0,1,0,2,0,3,0));
		p.setShape_180(Arrays.asList(0,0,0,1,0,2,0,3));
		p.setShape_270(Arrays.asList(0,0,1,0,2,0,3,0));
		classicPieces.add(p);
		
		p=new Piece(7, 2, 5, 4,Arrays.asList(0,1,1,0,1,1,1,2,2,1));
		p.setShape_90(Arrays.asList(0,1,1,0,1,1,1,2,2,1));
		p.setShape_180(Arrays.asList(0,1,1,0,1,1,1,2,2,1));
		p.setShape_270(Arrays.asList(0,1,1,0,1,1,1,2,2,1));
		classicPieces.add(p);
		
		p=new Piece(8, 2, 7, 2,Arrays.asList(0,0,1,0,1,1,1,2,1,3,2,0));
		p.setShape_90(Arrays.asList(0,0,0,1,0,2,1,1,2,1,3,1));
		p.setShape_180(Arrays.asList(0,3,1,0,1,1,1,2,1,3,2,3));
		p.setShape_270(Arrays.asList(0,1,1,1,2,1,3,0,3,1,3,2));
		classicPieces.add(p);
		
		p=new Piece(9, 1, 3, 2,Arrays.asList(0,0,0,1,1,1,1,2));
		p.setShape_90(Arrays.asList(0,1,1,0,1,1,2,0));
		p.setShape_180(Arrays.asList(0,0,0,1,1,1,1,2));
		p.setShape_270(Arrays.asList(0,1,1,0,1,1,2,0));
		classicPieces.add(p);
		
		p=new Piece(10, 0, 4, 2,Arrays.asList(0,1,0,2,0,3,1,0,1,1,1,2));
		p.setShape_90(Arrays.asList(0,0,1,0,1,1,2,0,2,1,3,1));
		p.setShape_180(Arrays.asList(0,1,0,2,0,3,1,0,1,1,1,2));
		p.setShape_270(Arrays.asList(0,0,1,0,1,1,2,0,2,1,3,1));
		classicPieces.add(p);
		
		
		p=new Piece(11, 1, 1, 5,Arrays.asList(0,0,0,1,0,2,0,3,1,0,1,3));
		p.setShape_90(Arrays.asList(0,0,0,1,1,1,2,1,3,0,3,1));
		p.setShape_180(Arrays.asList(0,0,0,3,1,0,1,1,1,2,1,3));
		p.setShape_270(Arrays.asList(0,0,0,1,1,0,2,0,3,0,3,1));
		classicPieces.add(p);
		
		p=new Piece(12, 0, 1, 2,Arrays.asList(0,0,0,1,0,2,1,0,1,2));
		p.setShape_90(Arrays.asList(0,0,0,1,1,1,2,0,2,1));
		p.setShape_180(Arrays.asList(0,0,0,2,1,0,1,1,1,2));
		p.setShape_270(Arrays.asList(0,0,0,1,1,0,2,0,2,1));
		classicPieces.add(p);		
		
		p=new Piece(13, 1, 0, 3,Arrays.asList(0,2,1,0,1,1,1,2,1,3,2,2));
		p.setShape_90(Arrays.asList(0,1,1,1,2,0,2,1,2,2,3,1));
		p.setShape_180(Arrays.asList(0,1,1,0,1,1,1,2,1,3,2,1));
		p.setShape_270(Arrays.asList(0,1,1,0,1,1,1,2,2,1,3,1));
		classicPieces.add(p);
		
		p=new Piece(14, 0, 1, 2,Arrays.asList(0,3,1,0,1,1,1,2,1,3,2,0));
		p.setShape_90(Arrays.asList(0,0,0,1,1,1,2,1,3,1,3,2));
		p.setShape_180(Arrays.asList(0,3,1,0,1,1,1,2,1,3,2,0));
		p.setShape_270(Arrays.asList(0,0,0,1,1,1,2,1,3,1,3,2));
		classicPieces.add(p);
		
		p=new Piece(15, 0, 2, 2,Arrays.asList(0,0,0,1,0,2));
		p.setShape_90(Arrays.asList(0,0,1,0,2,0));
		p.setShape_180(Arrays.asList(0,0,0,1,0,2));
		p.setShape_270(Arrays.asList(0,0,1,0,2,0));
		classicPieces.add(p);
		
		p=new Piece(16, 1, 4, 2,Arrays.asList(0,0,0,1,0,2,1,0));
		p.setShape_90(Arrays.asList(0,0,0,1,1,1,2,1));
		p.setShape_180(Arrays.asList(0,2,1,0,1,1,1,2));
		p.setShape_270(Arrays.asList(0,0,1,0,2,0,2,1));
		classicPieces.add(p);
		
		p=new Piece(17, 0, 2, 2,Arrays.asList(0,0,1,0,1,1,2,0));
		p.setShape_90(Arrays.asList(0,0,0,1,0,2,1,1));
		p.setShape_180(Arrays.asList(0,1,1,0,1,1,2,1));
		p.setShape_270(Arrays.asList(0,1,1,0,1,1,1,2));
		classicPieces.add(p);
		
		p=new Piece(18, 1, 2, 3,Arrays.asList(0,0,0,1,0,2,1,2,1,3));
		p.setShape_90(Arrays.asList(0,1,1,1,2,0,2,1,3,0));
		p.setShape_180(Arrays.asList(0,0,0,1,1,1,1,2,1,3));
		p.setShape_270(Arrays.asList(0,1,1,0,1,1,2,0,3,0));
		classicPieces.add(p);
		
		p=new Piece(19, 1, 3, 4,Arrays.asList(0,0,0,1,0,2,0,3,1,1));
		p.setShape_90(Arrays.asList(0,1,1,0,1,1,2,1,3,1));
		p.setShape_180(Arrays.asList(0,2,1,0,1,1,1,2,1,3));
		p.setShape_270(Arrays.asList(0,0,1,0,2,0,2,1,3,0));
		classicPieces.add(p);
		
		p=new Piece(20, 0, 1, 3,Arrays.asList(0,0,0,1,1,0));
		p.setShape_90(Arrays.asList(0,0,0,1,1,1));
		p.setShape_180(Arrays.asList(0,1,1,0,1,1));
		p.setShape_270(Arrays.asList(0,0,1,0,1,1));
		classicPieces.add(p);
		
		p=new Piece(21, 0, 3, 1,Arrays.asList(0,0,0,1,1,0));
		p.setShape_90(Arrays.asList(0,0,0,1,1,1));
		p.setShape_180(Arrays.asList(0,1,1,0,1,1));
		p.setShape_270(Arrays.asList(0,0,1,0,1,1));
		classicPieces.add(p);

		p=new Piece(22, 3, 8, 6,Arrays.asList(0,0,0,1,1,0,1,1,1,2,2,2));
		p.setShape_90(Arrays.asList(0,1,0,2,1,1,1,2,2,0,2,1));
		p.setShape_180(Arrays.asList(0,0,1,0,1,1,1,2,2,1,2,2));
		p.setShape_270(Arrays.asList(0,1,0,2,1,0,1,1,2,0,2,1));
		classicPieces.add(p);
		
		p=new Piece(23, 3, 10, 5,Arrays.asList(0,0,0,1,0,2,0,3,1,0,1,1));
		p.setShape_90(Arrays.asList(0,0,0,1,1,0,1,1,2,1,3,1));
		p.setShape_180(Arrays.asList(0,2,0,3,1,0,1,1,1,2,1,3));
		p.setShape_270(Arrays.asList(0,0,1,0,2,0,2,1,3,0,3,1));
		classicPieces.add(p);
		
		p=new Piece(24, 2, 4, 6,Arrays.asList(0,0,1,0,1,1,1,2));
		p.setShape_90(Arrays.asList(0,0,0,1,1,0,2,0));
		p.setShape_180(Arrays.asList(0,0,0,1,0,2,1,2));
		p.setShape_270(Arrays.asList(0,1,1,1,2,0,2,1));
		classicPieces.add(p);
		
		p=new Piece(25, 3, 7, 6,Arrays.asList(0,1,0,2,1,0,1,1));
		p.setShape_90(Arrays.asList(0,0,1,0,1,1,2,1));
		p.setShape_180(Arrays.asList(0,1,0,2,1,0,1,1));
		p.setShape_270(Arrays.asList(0,0,1,0,1,1,2,1));
		classicPieces.add(p);
		
		p=new Piece(26, 2, 5, 5,Arrays.asList(0,0,0,1,0,2,1,1,2,1));
		p.setShape_90(Arrays.asList(0,2,1,0,1,1,1,2,2,2));
		p.setShape_180(Arrays.asList(0,1,1,1,2,0,2,1,2,2));
		p.setShape_270(Arrays.asList(0,0,1,0,1,1,1,2,2,0));
		classicPieces.add(p);
		
		p=new Piece(27, 3, 10, 4,Arrays.asList(0,1,0,2,1,0,1,1,2,0));
		p.setShape_90(Arrays.asList(0,0,0,1,1,1,1,2,2,2));
		p.setShape_180(Arrays.asList(0,2,1,1,1,2,2,0,2,1));
		p.setShape_270(Arrays.asList(0,0,1,0,1,1,2,1,2,2));
		classicPieces.add(p);
		
		p=new Piece(29, 0, 2, 2,Arrays.asList(0,0,0,1,0,2,1,0,1,1));
		p.setShape_90(Arrays.asList(0,0,0,1,1,0,1,1,2,1));
		p.setShape_180(Arrays.asList(0,1,0,2,1,0,1,1,1,2));
		p.setShape_270(Arrays.asList(0,0,1,0,1,1,2,0,2,1));
		classicPieces.add(p);
		
		p=new Piece(28, 1, 7, 1,Arrays.asList(0,0,0,1,0,2,0,3,0,4));
		p.setShape_90(Arrays.asList(0,0,1,0,2,0,3,0,4,0));
		p.setShape_180(Arrays.asList(0,0,0,1,0,2,0,3,0,4));
		p.setShape_270(Arrays.asList(0,0,1,0,2,0,3,0,4,0));
		classicPieces.add(p);
		
		p=new Piece(30, 2, 10, 3,Arrays.asList(0,0,0,1,0,2,0,3,1,0));
		p.setShape_90(Arrays.asList(0,0,0,1,1,1,2,1,3,1));
		p.setShape_180(Arrays.asList(0,3,1,0,1,1,1,2,1,3));
		p.setShape_270(Arrays.asList(0,0,1,0,2,0,3,0,3,1));
		classicPieces.add(p);
		
		p=new Piece(31, 1, 1, 4,Arrays.asList(0,2,1,0,1,1,1,2,1,3,1,4,2,2));
		p.setShape_90(Arrays.asList(0,1,1,1,2,0,2,1,2,2,3,1,4,1));
		p.setShape_180(Arrays.asList(0,2,1,0,1,1,1,2,1,3,1,4,2,2));
		p.setShape_270(Arrays.asList(0,1,1,1,2,0,2,1,2,2,3,1,4,1));
		classicPieces.add(p);
		
		p=new Piece(32, 0, 2, 3,Arrays.asList(0,0,0,1,0,2,1,1,2,0,2,1,2,2));
		p.setShape_90(Arrays.asList(0,0,0,2,1,0,1,1,1,2,2,0,2,2));
		p.setShape_180(Arrays.asList(0,0,0,1,0,2,1,1,2,0,2,1,2,2));
		p.setShape_270(Arrays.asList(0,0,0,2,1,0,1,1,1,2,2,0,2,2));
		classicPieces.add(p);
	}
	
	public List<Piece> getClassicPieces()
	{
		return classicPieces;	
	}
	
	
}
