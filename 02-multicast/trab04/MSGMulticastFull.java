import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.String;

public class MSGMulticastFull{
  public static void main(String[] args) {
    DatagramSocket socketsend = null;
    DatagramPacket outPacket = null;
    BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
    String nickname;
    String msg;
    byte[] outBuf;
    final int PORT = 8888;

    try {
      long counter = 0;
      System.out.println("Escreva seu nome:");
      nickname = buffer.readLine().trim();
      InetAddress address = InetAddress.getByName("224.2.2.3");
      socketsend = new DatagramSocket();
      // NOTE: Receivers end in next line
      receive(nickname);
      System.out.println("ONLINE!");
      while (true) {
        msg = buffer.readLine().trim();
        msg = nickname+":"+msg;
        counter++;
        outBuf = msg.getBytes();
        //Send to multicast IP address and port
        outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);
        socketsend.send(outPacket);


      }
    } catch (IOException ioe) {
      System.out.println(ioe);
    }
  }

  private static void receive(String nickname){
    new Thread(new Runnable(){
      @Override
      public void run(){
        try{
          while(true){
            MulticastSocket socketreceive = null;
            DatagramPacket inPacket = null;
            byte[] inBuf = new byte[256];
            socketreceive = new MulticastSocket(8888);
            InetAddress address = InetAddress.getByName("224.2.2.3");
            String msg;

            socketreceive.joinGroup(address);
            inPacket = new DatagramPacket(inBuf, inBuf.length);
            socketreceive.receive(inPacket);
            msg = new String(inBuf, 0, inPacket.getLength());
            String[] finalmsg = msg.split(":");
            if (!finalmsg[0].equals(nickname)) {
              System.out.println(finalmsg[0] + ": " + finalmsg[1]);
            }
            try {
              Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
          }
        } catch (IOException ioe){
          System.out.println(ioe);
        }
      }
    }).start();
  }
}
