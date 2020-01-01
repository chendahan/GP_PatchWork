import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Piece {
	  private int id;
	  private int buttons;
	  private List<Dot> shape;
	  private int cost;
	  private int time;

	  List<Dot> shape_90;
	  List<Dot> shape_180;
	  List<Dot> shape_270;

	  Piece(int id, int buttons, int cost, int time,List<Integer> dots) {
	    this.id = id;
	    this.shape = createShape(dots);
	    this.setButtons(buttons);
	    this.cost = cost;
	    this.time = time;
	  }
	  
	  private List<Dot> createShape(List<Integer> dots) {
		  int i=dots.size();
		  List<Dot> list = new ArrayList<Dot>();
		  Dot dotToAdd;
		  while(i>0)
		  {
			  dotToAdd=new Dot(dots.get(i-2),dots.get(i-1));
			  list.add(dotToAdd);
			  i-=2;
		  }
		  
		return list;
	}
	  
	 public void setShape_90(List<Integer> dots)
	 {
		    this.shape_90 = createShape(dots);
	 }

	 public void setShape_180(List<Integer> dots)
	 {
		    this.shape_180 = createShape(dots);
	 }
	 
	 public void setShape_270(List<Integer> dots)
	 {
		    this.shape_270 = createShape(dots);
	 }
	 
	public int getId(){
		  return id;
	  }
	
	public List<Dot> getShape(){
		  return shape;
	  }

	public List<Dot> getShape_90(){
		  return shape_90;
	  }

	public List<Dot> getShape_180(){
		  return shape_180;
	  }
	
	public List<Dot> getShape_270(){
		  return shape_270;
	  }
	
	public int getButtons() {
		return buttons;
	}

	public void setButtons(int buttons) {
		this.buttons = buttons;
	}

	public int getCost() {
		return cost;
	}

	public int getTime() {
		return time;
	}
}

