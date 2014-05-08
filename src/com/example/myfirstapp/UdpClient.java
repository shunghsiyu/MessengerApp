package com.example.myfirstapp;

import java.net.*;
import java.io.*;


public class UdpClient implements Runnable {

	TcpSender ts;

	@Override
	public void run() {
		//ts.addReceiver();
	      while(!Thread.interrupted()){
	           try {
	        	   
	            InetAddress ip = InetAddress.getByName("0.0.0.0");
	            int port = 8888;

	            DatagramSocket getSocket = new DatagramSocket(port, ip);
	            getSocket.setBroadcast(true);

	            byte[] buf = new byte[1024];

	            DatagramPacket getPacket = new DatagramPacket(buf, buf.length);

	            getSocket.receive(getPacket);
	            
	            System.out.println("got data");
	            
	            String getMes = new String(buf, 0, buf.length);
	            
	            System.out.println(getMes);
	  
	            getSocket.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }	
	}

	public UdpClient(TcpSender ts) {
		this.ts = ts;
	}
}
