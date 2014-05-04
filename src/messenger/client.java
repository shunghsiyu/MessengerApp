package messenger;
import java.net.*;
import java.io.*;
public class client {
	 static String serverAddressStr = "224.0.0.1";
	    static int BROADCAST_PORT = 9898;
	    static int TTLTime = 255;

	    public static void main(String args[]) {
	        new client().run();
	    }
	    @Override
	    public void run() throws Exception {
	        ts.addReceiver(ip);
	        InetAddress serverAddress = InetAddress.getByName(serverAddressStr);
	        int port = BROADCAST_PORT;
	        MulticastSocket ms = new MulticastSocket();
	        ms.setTimeToLive(TTLTime);
	        byte[] message = new byte[100];
	        DatagramPacket dataPacket = new DatagramPacket(message, message.length, serverAddress, port);
	        ms.send(dataPacket);
	        ms.close();

	    }
	    
	      public client(TcpSender ts) {

	    }
}
