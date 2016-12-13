package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.LinkedList;

import Utility.Builder;
import Utility.FullAddress;
import Utility.Messages;

public class ContactThread extends Thread{
	private FullAddress fullAddress;
	private Server server;
	private DatagramSocket socket;
	
	public ContactThread(DatagramPacket p, Server s)
	{
		fullAddress = new FullAddress(p.getPort(), p.getAddress());
		server = s;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		String s = getGameString();
		DatagramPacket p = Builder.buildPacket(Messages.GAME_LIST_PACKET, s, fullAddress);
		try {
			socket.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte b[] = new byte[500];
		p = new DatagramPacket(b, b.length);
		while(true)
		{
			try {
				socket.receive(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(p.getData()[1] == Messages.JOIN_REQUEST)
			{
				System.out.println("C: Valid Join Packet.");
				String name = Messages.getMessage(p.getData());
				if(server.gameExists(name))
				{
					System.out.println("C: Game exists. and can be joined.");
					server.joinGame(name, p);
				}
				else
				{
					System.out.println("C: Game does not exist.");
				}
				break;
			}
			else if(p.getData()[1] == Messages.CREATE_GAME)
			{
				System.out.println("C: Valid Game Game Creation Packet.");
				String name = Messages.getMessage(p.getData());
				if(server.gameExists(name))
				{
					System.out.println("C: Game exists. and cannot be created.");
				}
				else
				{
					System.out.println("C: Game does not exists. and can be created.");
					GameThread g = new GameThread(name, p);
					server.addGame(g);
					p = Builder.buildPacket(Messages.GAME_CREATED, fullAddress);
					try {
						socket.send(p);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
			}
			else if(p.getData()[1] == Messages.REFRESH_PACKET)
			{
				System.out.println("C: Valid Refresh Packet.");
				s = getGameString();
				p = Builder.buildPacket(Messages.GAME_LIST_PACKET, s, fullAddress);
				try {
					socket.send(p);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("C: Invalid Packet.");
			}
		}
//		try {
//			socket.receive(p);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	public String getGameString()
	{
		LinkedList<GameThread> games = server.getGames();
		String s = "";
		for(GameThread g: games)
		{
			s += g.getGameName() + "," + g.getPlayerCount() + "\n";
		}
		return s.equals("") ? "Empty" : s;
	}
}
