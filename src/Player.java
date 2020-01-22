
public class Player {
	private int id;
	private int buttons;
	PlayerBoard playerBoard;
	private boolean hasSevenBySeven;
	private int score;
	private int position;
	private int lastButtonIndex;

    public Player(int id) {
        this.id = id;
        this.buttons = 5;
        this.hasSevenBySeven = false;
        this.playerBoard = new PlayerBoard(this);
        this.position = 0;
        // save the index of the last button position the player visited
        this.lastButtonIndex = -1; // initialized to -1 because initially no buttons were collected
    }

    public boolean hasSevenBySeven() {
        return hasSevenBySeven;
    }

    public int getPosition() {
        return position;
    }

    public int getLastButtonIndex() {
        return lastButtonIndex;
    }

    public void setLastButtonIndex(int idx) {
        lastButtonIndex = idx;
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

	public void printBoard() 
	{
		this.playerBoard.print();
		
	}
}
