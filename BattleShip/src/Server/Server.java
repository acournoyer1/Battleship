package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.LinkedList;

import Utility.Messages;

public class Server extends Thread{
	public DatagramSocket socket;
	private LinkedList<GameThread> games;
	
	public final int serverPort = 2000;
	
	public Server()
	{
		try {
			socket = new DatagramSocket(serverPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		games = new LinkedList<GameThread>();
	}
	
	@Override
	public void run()
	{
		System.out.println("S: Server Running...");
		while(true)
		{
			byte[] b = new byte[2];
			DatagramPacket p = new DatagramPacket(b, b.length);
			try {
				socket.receive(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(p.getData()[1] == Messages.SERVER_REQUEST)
			{
				System.out.println("S: Valid Server Request Packet.");
				new ContactThread(p, this).start();
			}
			else
			{
				System.out.println("S: Invalid Server Request Packet.");
			}
		}
	}
	
	public synchronized LinkedList<GameThread> getGames()
	{
		return new LinkedList<GameThread>(games);
	}
	
	public synchronized boolean gameExists(String name)
	{
		for(GameThread g: getGames())
		{
			if(g.getGameName().equals(name))
			{
				return true;
			}
		}
		return false;
	}
	
	public synchronized void joinGame(String name, DatagramPacket p)
	{
		for(GameThread g: getGames())
		{
			if(g.getGameName().equals(name))
			{
				g.join(p);
				g.start();
				return;
			}
		}
	}
	
	public synchronized void addGame(GameThread g)
	{
		games.add(g);
	}
	
	public static void main(String args[])
	{
		new Server().start();
	}
}
