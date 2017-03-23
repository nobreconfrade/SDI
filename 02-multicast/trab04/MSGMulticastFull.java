import java.io.*;
import java.net.*;

public class MSGMulticastFull extends Thread{
  public static void main(String[] args) {
    DatagramSocket socket = null;
    DatagramPacket outPacket = null;
    BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
    String nickname;
    String msg;
    byte[] outBuf;
    final int PORT = 8888;

    try {
      socket = new DatagramSocket();
      long counter = 0;
      System.out.println("Escreva seu nome:");
      nickname = buffer.readLine().trim();

      while (true) {
        System.out.println("Escreva sua mensagem:");
        msg = buffer.readLine().trim();
        counter++;
        outBuf = msg.getBytes();
        //Send to multicast IP address and port
        InetAddress address = InetAddress.getByName("224.2.2.3");
        outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);

        socket.send(outPacket);

        try {
          Thread.sleep(500);
        } catch (InterruptedException ie) {
        }
      }
    } catch (IOException ioe) {
      System.out.println(ioe);
    }
  }
}
