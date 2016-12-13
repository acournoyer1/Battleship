package Utility;

public interface Messages {
	public static final byte SERVER_REQUEST = 1;
	public static final byte GAME_LIST_PACKET = 2;
	public static final byte JOIN_REQUEST = 3;
	public static final byte CREATE_GAME = 4;
	public static final byte REFRESH_PACKET = 5;
	public static final byte GAME_CREATED = 6;
	public static final byte GAME_JOINED = 7;
	public static final byte GAME_REJECTED = 8;
	
	
	public static String getMessage(byte[] b)
	{
		if(b[1] == REFRESH_PACKET || b[1] == SERVER_REQUEST) return null;
		int length = b.length;
		for(int i = 2; i < b.length; i++)
		{
			if(b[i] == 0)
			{
				length = i;
				break;
			}
		}
		byte[] data = new byte[length-2];
		for(int i = 0, j = 2; i < data.length && j < b.length; i++, j++)
		{
			data[i] = b[j];
		}
		return new String(data);
	}
}
