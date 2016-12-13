package Player.Setup;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.*;

import org.apache.commons.validator.routines.InetAddressValidator;

import Utility.Messages;

public class StartMenu  extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField iPField;
	private JButton connectButton;
	private JButton cancelButton;
	private DatagramSocket socket;
	private InetAddressValidator validator = InetAddressValidator.getInstance();
	
	public StartMenu()
	{
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		JLabel label = new JLabel("Server IP: ");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		iPField = new JTextField("192.168.0.103");
		iPField.setColumns(10);
		connectButton = new JButton("Connect");
		cancelButton = new JButton("Cancel");
		JPanel top = new JPanel();
		JPanel bottom = new JPanel();
		top.add(label);
		top.add(iPField);
		bottom.add(cancelButton);
		bottom.add(connectButton);
		this.add(top, BorderLayout.CENTER);
		this.add(bottom, BorderLayout.SOUTH);
		this.setSize(300,100);
		this.setResizable(false);
		this.setTitle("Connect to Server");
		this.setLocationRelativeTo(null);
		setUpListeners();
		connectButton.setEnabled(true);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	private void setUpListeners()
	{
		cancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				dispose();	
			}			
		});
		connectButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				byte b[] = new byte[500];
				b[1] = Messages.SERVER_REQUEST;
				DatagramPacket p = null;
				try {
					p = new DatagramPacket(b, b.length, InetAddress.getByName(iPField.getText()), 2000);
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
				try {
					socket.send(p);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					socket.receive(p);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if(p.getData()[1] == Messages.GAME_LIST_PACKET)
				{
					System.out.println("Valid Game List Packet.");
					new GameSelector(socket, p);
					dispose();
				}
				else
				{
					System.out.println("Invalid Game List Packet.");
				}
			}		
		});
		iPField.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if(!(e.getKeyCode() == KeyEvent.VK_ENTER))
				{
					if(validator.isValid(iPField.getText()))
					{
						connectButton.setEnabled(true);
					}
				}
				else if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					connectButton.doClick();
				}
			}
		});
	}
	
	public static void main(String args[])
	{
		new StartMenu();
	}
}
