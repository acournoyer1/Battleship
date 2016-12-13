package Player.Setup;

public class Game {
	private String name;
	private int playerCount;
	
	public Game(String name, int playerCount)
	{
		this.name = name;
		this.playerCount = playerCount;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getPlayerCount()
	{
		return playerCount;
	}
	
	public String toString()
	{
		String s = playerCount == 1 ? " player " : " players ";
		return name + "   " + playerCount + s + "out of 2"; 
	}
}
