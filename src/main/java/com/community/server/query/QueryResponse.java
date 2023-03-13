package com.community.server.query;

import java.util.ArrayList;

public class QueryResponse
{
	private int onlinePlayers;
	private ArrayList<String> playerList;
	
	public QueryResponse(byte[] data)
	{
		if(data == null) return;
		
		data = ByteUtils.trim(data);
		byte[][] temp = ByteUtils.split(data);

		onlinePlayers	= Integer.parseInt(new String(temp[15]));
		playerList = new ArrayList<>();

		for(int i=25; i<temp.length; i++)
		{
			playerList.add(new String(temp[i]));
		}
	}

	public int getOnlinePlayers()
	{
		return onlinePlayers;
	}

	/**
	 * Returns an <code>ArrayList</code> of strings containing the connected players' usernames.
	 * Note that this will return null for basic status requests.
	 * @return An <code>ArrayList</code> of player names
	 */
	public ArrayList<String> getPlayerList()
	{
		return playerList;
	}
	
	//TODO getPlayers return hashmap/array/arraylist
}
