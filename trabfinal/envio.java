import java.io.*;
import java.net.*;
import java.util.ArrayList;

class envio {

    public static void main(String args[]) throws Exception {
        FileInputStream listaips = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        String sentence = "";
        Metadado meta = new Metadado();
        FileInputStream file = null;
        file = new FileInputStream(args[0]);
        try {

            byte[] bytes = new byte[(int)file.getChannel().size()];
            file.read(bytes);
            ByteArrayInputStream bos = new ByteArrayInputStream(bytes);
            ObjectInput in   = new ObjectInputStream(bos);
            meta.nome        = in.readUTF();
            meta.size        = in.readLong();
            meta.n_chunks    = in.readInt();
            meta.vetor_dados = (ArrayList <Dados>) in.readObject();

            listaips = new FileInputStream("listaips.txt");
            int count;
            while ((count = listaips.read()) != -1){
              sentence += Character.toString((char)count);
            }
            String[] lines = sentence.split(System.getProperty("line.separator"));
            for(int i=0;i<lines.length;i++)
              System.out.println(lines[i]);
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress
                    = InetAddress.getByName("192.168.0.9");
            byte[] sendData = new byte[65507];
            byte[] receiveData = new byte[65507];
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
