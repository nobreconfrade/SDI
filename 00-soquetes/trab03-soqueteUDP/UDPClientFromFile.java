import java.io.*;
import java.net.*;

class UDPClientFromFile {

    public static void main(String args[]) throws Exception {
        FileInputStream in = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        String sentence = "";
        try {
            in = new FileInputStream("dontgit.txt");
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress
                    = InetAddress.getByName("localhost");
            byte[] sendData = new byte[65507];
            byte[] receiveData = new byte[65507];
            int count;
            while ((count = in.read()) != -1){
              sentence += Character.toString((char)count);
            }
            // NOTE: Next lines is for test purposes
            System.out.print(sentence);
            System.out.println("---------------------------------------------------------");
            sendData = sentence.getBytes();
            System.out.println(sendData.length);
            DatagramPacket sendPacket
                    = new DatagramPacket(sendData, sendData.length,
                            IPAddress, 9876);
            clientSocket.send(sendPacket);
            DatagramPacket receivePacket
                    = new DatagramPacket(receiveData,
                            receiveData.length);
            clientSocket.receive(receivePacket);
            String modifiedSentence
                    = new String(receivePacket.getData());
            System.out.println("FROM SERVER:"
                    + modifiedSentence);
            clientSocket.close();
      }catch (FileNotFoundException e) {
        e.printStackTrace();
      }catch (IOException e) {
        e.printStackTrace();
      }
    }
}
