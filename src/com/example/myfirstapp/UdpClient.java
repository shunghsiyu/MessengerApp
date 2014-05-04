package com.example.myfirstapp;

import java.net.*;
import java.io.*;


public class UdpClient implements Runnable {
	static String serverAddressStr = "224.0.0.1";
	static int BROADCAST_PORT = 9898;
	static int TTLTime = 255;
	TcpSender ts;

	@Override
	public void run() {
		//ts.addReceiver(ip);
		InetAddress serverAddress = null;
		try {
			serverAddress = InetAddress.getByName(serverAddressStr);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int port = BROADCAST_PORT;
		MulticastSocket ms = null;
		try {
			ms = new MulticastSocket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ms.setTimeToLive(TTLTime);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] message = new byte[100];
		DatagramPacket dataPacket = new DatagramPacket(message, message.length,
				serverAddress, port);
		try {
			ms.send(dataPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ms.close();

	}

	public UdpClient(TcpSender ts) {
		this.ts = ts;
	}
}
