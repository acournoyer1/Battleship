package Tests;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import Player.Setup.WaitDialog;
import Server.GameThread;
import Server.Server;
import Utility.FullAddress;

public interface QuickStart {
	public static void main(String args[])
	{
		try {
			QuickStart.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void start() throws Exception
	{
		Server s = new Server();
		
		DatagramSocket s1 = new DatagramSocket();
		DatagramSocket s2 = new DatagramSocket();
		
		InetAddress address = InetAddress.getByName("192.168.0.103");
		FullAddress player1 = new FullAddress(s1.getLocalPort(), address);
		FullAddress player2 = new FullAddress(s2.getLocalPort(), address);
		
		GameThread g = new GameThread();
		g.setAddresses(player1, player2);
		
		new WaitDialog(s1).start();
		new WaitDialog(s2).start();
		Thread.sleep(100);
		g.start();
	}
}
