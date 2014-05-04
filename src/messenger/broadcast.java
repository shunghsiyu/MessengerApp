package messenger;
import java.net.*;
import java.io.*;
public class broadcast {
	private static String ip;
    static int RECIEVE_LENGTH=1024;
    static int BROADCAST_PORT = 9898;
    static String BROADCAST_IP = "224.0.0.1";
    InetAddress inetAddress = null;

    public static void main(String args[]) throws IOException, Exception{
        new broadcast().run();
    }
    @Override
    public void run()throws Exception{
        InetAddress receiveAddress=InetAddress.getByName(BROADCAST_IP);
        int port=BROADCAST_PORT;
        MulticastSocket receiveMulticast=new MulticastSocket(port);
        receiveMulticast.joinGroup(receiveAddress);
        DatagramPacket clientIP=new DatagramPacket(new byte[RECIEVE_LENGTH],RECIEVE_LENGTH);
        receiveMulticast.receive(clientIP);
        receiveMulticast.close();
      
    
    }

   
}
