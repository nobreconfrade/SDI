import java.io.*;
import java.net.*;
/**
 * @author lycog
 */
public class MulticastSender {
  public static void main(String[] args) {
    DatagramSocket socket = null;
    DatagramPacket outPacket = null;
    is = new DataInputStream(clientSocket.getInputStream());
    byte[] outBuf;
    final int PORT = 8888;

    try {
      socket = new DatagramSocket();
      long counter = 0;
      String msg;
      System.out.println("Escreva seu nome:");
      String name = is.readLine().trim();

      while (true) {
        System.out.println("Escreva sua mensagem:");
        msg = is.readLine().trim();
        counter++;
        outBuf = msg.getBytes();

        //Send to multicast IP address and port
        InetAddress address = InetAddress.getByName("224.2.2.3");
        outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);

        socket.send(outPacket);

        System.out.println("Server sends : " + msg);
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
