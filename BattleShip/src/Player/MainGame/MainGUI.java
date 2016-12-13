package Player.MainGame;

import java.net.DatagramSocket;

import javax.swing.JFrame;

public class MainGUI extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private DatagramSocket socket;
	
	public MainGUI(DatagramSocket socket)
	{
		this.socket = socket;
		
		this.setTitle("Battleship");
		this.setSize(1000, 1000);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
}
