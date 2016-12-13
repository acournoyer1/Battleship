package Utility;

import java.net.InetAddress;

public class FullAddress {
	private int port;
	private InetAddress address;
	
	public FullAddress(int port, InetAddress address)
	{
		this.port = port;
		this.address = address;
	}
	
	public int getPort()
	{
		return port;
	}
	
	public InetAddress getAddress()
	{
		return address;
	}
}
