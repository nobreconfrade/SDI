
import java.io.*;
import java.net.*;

class UDPServerFromFile {

    public static void main(String args[]) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] receiveData = new byte[65507];
        byte[] sendData = new byte[65507];
        while (true) {
            DatagramPacket receivePacket
                    = new DatagramPacket(receiveData,
                            receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String(
                    receivePacket.getData());
            InetAddress IPAddress
                    = receivePacket.getAddress();
            int port = receivePacket.getPort();
            // NOTE: quote: "aqui tinha um 'UDPServer:' safado que tava aumentando o numero de bytes em 11"
            String capitalizedSentence
                    = sentence.toUpperCase();
            sendData = capitalizedSentence.
                    getBytes();
            DatagramPacket sendPacket
                    = new DatagramPacket(sendData,
                            sendData.length,
                            IPAddress, port);
            serverSocket.send(sendPacket);
        }
    }
}
