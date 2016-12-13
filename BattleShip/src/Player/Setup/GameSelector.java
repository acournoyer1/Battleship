package Player.Setup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Player.MainGame.MainGUI;
import Utility.Builder;
import Utility.FullAddress;
import Utility.Messages;

public class GameSelector extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DatagramSocket socket;
	private FullAddress fullAddress;
	private JList<Game> gameList;
	private JButton refreshButton;
	private JButton joinButton;
	private JButton cancelButton;
	private JButton createButton;
	private JTextField gameName;
	
	public GameSelector(DatagramSocket socket, DatagramPacket p)
	{
		this.refreshButton = new JButton("Refresh");
		this.joinButton = new JButton("Join");
		this.cancelButton = new JButton("Cancel");
		this.createButton = new JButton("Create");
		this.gameList = new JList<Game>(getGames(p.getData()));
		this.gameName = new JTextField();
		this.gameName.setColumns(10);
		this.socket = socket;
		this.fullAddress = new FullAddress(p.getPort(), p.getAddress());
		
		this.joinButton.setEnabled(false);
		this.createButton.setEnabled(false);
		
		this.add(getRefreshPanel(), BorderLayout.NORTH);
		this.add(getGamePanel(), BorderLayout.CENTER);
		this.add(getButtonPanel(), BorderLayout.SOUTH);
		this.getContentPane().setPreferredSize(new Dimension(500,200));
		this.pack();
		this.setResizable(false);
		this.setTitle("Connect to Server");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addListeners();
		this.setVisible(true);
	}
	
	private void addListeners()
	{
		gameName.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
				boolean set = false;
				if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
				{
					if(gameName.getText().length() <= 1)
					{
						set = false;
					}
					else
					{
						set = true;
					}
				}
				else if(Character.isLetter(e.getKeyChar()))
				{
					set = true;
				}
				createButton.setEnabled(set);
			}
		});
		createButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				DatagramPacket p = Builder.buildPacket(Messages.CREATE_GAME, gameName.getText(), fullAddress);
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
				if(p.getData()[1] == Messages.GAME_CREATED)
				{
					System.out.println("Game created.");
					new WaitDialog(socket).start();
					dispose();
				}
			}
		});
		joinButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				DatagramPacket p = Builder.buildPacket(Messages.JOIN_REQUEST, gameList.getSelectedValue().getName(), fullAddress);
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
				if(p.getData()[1] == Messages.GAME_JOINED)
				{
					System.out.println("Game joined.");
					new MainGUI(socket);
					dispose();
				}
			}
		});
		refreshButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				DatagramPacket p = Builder.buildPacket(Messages.REFRESH_PACKET, fullAddress);
				try {
					socket.send(p);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				byte b[] = new byte[500];
				p = new DatagramPacket(b, b.length);
				try {
					socket.receive(p);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if(p.getData()[1] == Messages.GAME_LIST_PACKET)
				{
					gameList = new JList<Game>(getGames(p.getData()));
				}
			}
		});
		gameList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent arg0) 
			{
				if(gameList.isSelectionEmpty())
				{
					joinButton.setEnabled(false);
				}
				else if(gameList.getSelectedValue().getPlayerCount() < 2)
				{
					joinButton.setEnabled(true);
				}
				else
				{
					joinButton.setEnabled(false);
				}
			}
		});
	}
	
	private JSplitPane getButtonPanel()
	{
		JPanel right = new JPanel();
		right.add(joinButton);
		right.add(createButton);
		JPanel cancelPanel = new JPanel();
		cancelPanel.add(cancelButton);
		JSplitPane left = new JSplitPane();
		left.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		left.setLeftComponent(cancelPanel);
		left.setRightComponent(new JLabel());
		left.setDividerLocation(100);
		left.setDividerSize(0);
		left.setBorder(BorderFactory.createEmptyBorder());
		JSplitPane split = new JSplitPane();
		split.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		split.setLeftComponent(left);
		split.setRightComponent(right);
		split.setDividerLocation(300);
		split.setDividerSize(0);
		split.setBorder(BorderFactory.createEmptyBorder());
		return split;
	}
	
	private JSplitPane getRefreshPanel()
	{
		JPanel refreshPanel = new JPanel();
		refreshPanel.add(refreshButton);
		JSplitPane split = new JSplitPane();
		split.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		split.setLeftComponent(new JLabel());
		split.setRightComponent(refreshPanel);
		split.setDividerLocation(300);
		split.setDividerSize(0);
		split.setBorder(BorderFactory.createEmptyBorder());
		return split;
	}
	
	private JSplitPane getGamePanel()
	{
		JPanel gamePanel = new JPanel();
		gamePanel.add(new JLabel("Game Name: "));
		gamePanel.add(gameName);
		JScrollPane scroll = new JScrollPane(gameList);
		JSplitPane split = new JSplitPane();
		split.setOrientation(JSplitPane.VERTICAL_SPLIT);
		split.setTopComponent(scroll);
		split.setBottomComponent(gamePanel);
		split.setDividerLocation(90);
		split.setDividerSize(0);
		split.setBorder(BorderFactory.createEmptyBorder());
		return split;
	}
	
	private Game[] getGames(byte[] b)
	{
		String gameString = Messages.getMessage(b);
		if(gameString.equals("Empty")) return new Game[0];
		String[] gameSplit = gameString.split("\n");
		Game[] games = new Game[gameSplit.length];
		for(int i = 0; i < gameSplit.length; i++)
		{
			String[] game = gameSplit[i].split(",");
			games[i] = new Game(game[0], Integer.parseInt(game[1]));
		}
		return games;
	}
	
}
