
public class Player {
	private int id;
	private int buttons;
	PlayerBoard playerBoard;
	private boolean hasSevenBySeven;
	private int score;

	  Player(int id) {
	    this.id = id;
	    this.buttons = 5;
	    this.hasSevenBySeven = false;
	    this.playerBoard = new PlayerBoard(this);
	  }
}
