package com.example.myfirstapp;

import java.net.*;
import java.io.*;


public class UdpBroadcaster implements Runnable {
	private String server_ip;
	
	
	@Override
	public void run() {
		 while (!Thread.interrupted()) {
	            try {
                 
	                DatagramSocket sendSocket = new DatagramSocket();
	                sendSocket.setBroadcast(true);

	                String server_ip = InetAddress.getLocalHost().toString();

	                byte[] buf = server_ip.getBytes();

	                int port = 8888;
	                InetAddress ip = InetAddress.getByName("255.255.255.255");

	                DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, ip, port);

	                sendSocket.send(sendPacket);
	                System.out.println("data sent");
	                sendSocket.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }

		 }
	}
}

