package Utility;

import java.net.DatagramPacket;

public interface Builder {
	public static DatagramPacket buildPacket(byte opcode, String s, FullAddress f)
	{
		byte[] text = s.getBytes();
		byte[] msg = new byte[text.length+2];
		msg[1] = opcode;
		for(int i = 0, j = 2; i < text.length && j < msg.length; i++, j++)
		{
			msg[j] = text[i];
		}
		return new DatagramPacket(msg, msg.length, f.getAddress(), f.getPort());
	}
	
	public static DatagramPacket buildPacket(byte opcode, FullAddress f)
	{
		byte[] msg = new byte[2];
		msg[1] = opcode;
		return new DatagramPacket(msg, msg.length, f.getAddress(), f.getPort());
	}
}
