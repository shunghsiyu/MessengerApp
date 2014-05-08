package com.example.myfirstapp;

import java.net.*;
import java.io.*;

public class UdpClient implements Runnable {
    private TcpSender ts;
    
    @Override
    public void run() {
    	DatagramSocket getSocket = null;
        while (!Thread.interrupted()) {
            try {
            	getSocket = new DatagramSocket(UdpBroadcaster.PORT);
                getSocket.setSoTimeout(100);
            	getSocket.setBroadcast(true);

                byte[] buf = new byte[1024];

                DatagramPacket getPacket = new DatagramPacket(buf, buf.length);

                getSocket.receive(getPacket);
                
                System.out.println("Received broadcast from " + getPacket.getAddress());
                ts.addReceiver(getPacket.getAddress());

            } catch (UnknownHostException e) {
            	System.out.println("Host unknown");
				e.printStackTrace();
			} catch (SocketException e) {
				e.printStackTrace();
				System.out.println("Failed to open socket");
			} catch(SocketTimeoutException e) {
				// Ignore
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Failed to received packet");
			} finally {
				if(getSocket != null) {
					getSocket.close();
					getSocket = null;
				}
			}
        }
    }

    public UdpClient(TcpSender ts) {
        this.ts = ts;
    }
}
