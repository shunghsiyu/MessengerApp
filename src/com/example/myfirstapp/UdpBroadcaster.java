package com.example.myfirstapp;

import java.io.IOException;
import java.net.*;

public class UdpBroadcaster implements Runnable {
	public static int PORT = 10332;
    public static String BROADCAST_ADDRESS = "255.255.255.255";

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                DatagramSocket sendSocket = new DatagramSocket();
                sendSocket.setBroadcast(true);

                byte[] buf = "".getBytes();

                InetAddress ip = InetAddress.getByName(BROADCAST_ADDRESS);

                DatagramPacket sendPacket = new DatagramPacket(buf, buf.length,
                        ip, PORT);

                sendSocket.send(sendPacket);
                sendSocket.close();
                
            } catch (UnknownHostException e) {
				System.out.println("Can't get broadcast address");
				e.printStackTrace();
			} catch (SocketException e) {
				System.out.println("Error opening socket");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Can't send packet");
				e.printStackTrace();
			}
            
            try {    
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                // Ignore
            }
        }
    }

}
  