package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Utility.Builder;
import Utility.FullAddress;
import Utility.Messages;

public class GameThread extends Thread{
	private String name;
	private int playerCount = 1;
	private FullAddress player1;
	private FullAddress player2;
	private DatagramSocket socket;
	
	public GameThread(String name, DatagramPacket p)
	{
		this();
		this.name = name;
		this.player1 = new FullAddress(p.getPort(), p.getAddress());
	}
	
	/**
	 * WARNING: Only use this constructor for testing
	 */
	public GameThread()
	{
		try {
			this.socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public String getGameName()
	{
		return name;
	}
	
	public int getPlayerCount()
	{
		return playerCount;
	}
	
	public void join(DatagramPacket p)
	{
		this.player2 = new FullAddress(p.getPort(), p.getAddress());
		playerCount++;
	}
	
	/**
	 * WARNING: Only use this method when testing.
	 * @param player1
	 * @param player2
	 */
	public void setAddresses(FullAddress player1, FullAddress player2)
	{
		this.player1 = player1;
		this.player2 = player2;
	}
	
	@Override
	public void run()
	{
		System.out.println("G: Game Started.");
		DatagramPacket p = Builder.buildPacket(Messages.GAME_JOINED, player1);
		try {
			socket.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
		p = Builder.buildPacket(Messages.GAME_JOINED, player2);
		try {
			socket.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
