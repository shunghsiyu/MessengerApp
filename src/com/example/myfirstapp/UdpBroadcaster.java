package com.example.myfirstapp;

import java.net.*;
import java.io.*;


public class UdpBroadcaster implements Runnable {
	private static String ip;
	static int RECIEVE_LENGTH = 1024;
	static int BROADCAST_PORT = 9898;
	static String BROADCAST_IP = "224.0.0.1";
	InetAddress inetAddress = null;

	@Override
	public void run() {
		try {
			InetAddress receiveAddress = InetAddress.getByName(BROADCAST_IP);
			int port = BROADCAST_PORT;
			MulticastSocket receiveMulticast = new MulticastSocket(port);
			receiveMulticast.joinGroup(receiveAddress);
			DatagramPacket clientIP = new DatagramPacket(
					new byte[RECIEVE_LENGTH], RECIEVE_LENGTH);
			receiveMulticast.receive(clientIP);
			receiveMulticast.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
