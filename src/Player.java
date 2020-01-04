
public class Player {
	private int id;
	private int buttons;
	PlayerBoard playerBoard;
	private boolean hasSevenBySeven;
	private int score;
	private int position;

  public Player(int id) {
    this.id = id;
    this.buttons = 5;
    this.hasSevenBySeven = false;
    this.playerBoard = new PlayerBoard(this);
    this.position = 0;
  }
  
  public int getPosition() {
	  return position;
  }
  
  public void setPosition(int position) {
	  this.position = position;
  }
  
  public int getButtons() {
	  return buttons;
  }
  
  public void setButtons(int buttons) {
	  this.buttons = buttons;
  }
  
  public PlayerBoard getPlayerBoard() {
	  return playerBoard;
  }
	  
	  
}
