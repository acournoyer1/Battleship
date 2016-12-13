package Player.Setup;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.swing.JDialog;
import javax.swing.JLabel;

import Player.MainGame.MainGUI;
import Utility.Messages;

public class WaitDialog extends Thread{
	private DatagramSocket socket;
	private JDialog dialog;
	
	public WaitDialog(DatagramSocket socket)
	{
		this.socket = socket;
		this.dialog = new JDialog();
		
		dialog.add(new JLabel("\n   Waiting for a second player to join...   \n"));
		dialog.pack();
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
	
	@Override
	public void run()
	{
		byte[] b = new byte[2];
		DatagramPacket p = new DatagramPacket(b, b.length);
		try {
			socket.receive(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(p.getData()[1] == Messages.GAME_JOINED)
		{
			System.out.println("Game joined.");
			new MainGUI(socket);
			dialog.dispose();
		}
	}
}
