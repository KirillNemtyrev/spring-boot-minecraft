package com.community.server.query;

import java.net.*;

/**
 * A class that handles Minecraft Query protocol requests
 *
 * @author Ryan McCann
 */
public class MCQuery
{
	final static byte HANDSHAKE = 9;
	final static byte STAT = 0;

	String serverAddress;
	int queryPort; // the default minecraft query port

	int localPort = 25566; // the local port we're connected to the server on

	private DatagramSocket socket = null; //prevent socket already bound exception
	private int token;

	public MCQuery(String address, int port)
	{
		serverAddress = address;
		queryPort = port;
	}

	// used to get a session token
	private void handshake()
	{
		QueryRequest req = new QueryRequest();
		req.type = HANDSHAKE;
		req.sessionID = generateSessionID();

		int val = 11 - req.toBytes().length; //should be 11 bytes total
		byte[] input = ByteUtils.padArrayEnd(req.toBytes(), val);
		byte[] result = sendUDP(input);

		if (result == null){
			return;
		}

		token = Integer.parseInt(new String(result).trim());
	}

	/**
	 * Use this to get more information, including players, from the server.
	 * @return a <code>QueryResponse</code> object
	 */
	public QueryResponse fullStat()
	{
		handshake();

		QueryRequest req = new QueryRequest();
		req.type = STAT;
		req.sessionID = generateSessionID();
		req.setPayload(token);
		req.payload = ByteUtils.padArrayEnd(req.payload, 4); //for full stat, pad the payload with 4 null bytes

		byte[] send = req.toBytes();

		byte[] result = sendUDP(send);

		/*
		 * note: buffer size = base + #players(online) * 16(max username length)
		 */

		return new QueryResponse(result);
	}

	private byte[] sendUDP(byte[] input)
	{
		try
		{
			while(socket == null)
			{
				try {
					socket = new DatagramSocket(localPort); //create the socket
				} catch (BindException e) {
					++localPort; // increment if port is already in use
				}
			}

			//create a packet from the input data and send it on the socket
			InetAddress address = InetAddress.getByName(serverAddress); //create InetAddress object from the address
			DatagramPacket packet1 = new DatagramPacket(input, input.length, address, queryPort);
			socket.send(packet1);

			//receive a response in a new packet
			byte[] out = new byte[1024]; //TODO guess at max size
			DatagramPacket packet = new DatagramPacket(out, out.length);
			socket.setSoTimeout(500); //one half second timeout
			socket.receive(packet);

			return packet.getData();
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		catch (SocketTimeoutException e)
		{
			//System.err.println("Socket Timeout! Is the server offline?");
			//System.exit(1);
			// throw exception
		}
		catch (UnknownHostException e)
		{
			System.err.println("Unknown host!");
			e.printStackTrace();
			//System.exit(1);
			// throw exception
		}
		catch (Exception e) //any other exceptions that may occur
		{
			e.printStackTrace();
		}

		return null;
	}

	public void close(){
		socket.close();
	}

	private int generateSessionID()
	{
		/*
		 * Can be anything, so we'll just use 1 for now. Apparently it can be omitted altogether.
		 * TODO: increment each time, or use a random int
		 */
		return 1;
	}

}
