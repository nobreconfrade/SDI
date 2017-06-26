import java.io.*;
import java.net.*;
import java.util.ArrayList;

class envio {
  private final static int serverPort = 3248;

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
            for(int i=0;i<lines.length;i++){
                byte[] aByte = new byte[1];
                int bytesRead;

                Socket clientSocket = null;
                InputStream is = null;

                try {
                  // TODO: decidir como sera feito o envio, pelos IPs ou pelos chunks
                    clientSocket = new Socket( lines[i] , serverPort );
                    is = clientSocket.getInputStream();
                } catch (IOException ex) {
                    System.out.println("Alguma coisa deu ruim com o getInputStream");
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                if (is != null) {

                    FileOutputStream fos = null;
                    BufferedOutputStream bos = null;
                    try {
                        fos = new FileOutputStream( fileOutput );
                        bos = new BufferedOutputStream(fos);
                        bytesRead = is.read(aByte, 0, aByte.length);

                        do {
                                baos.write(aByte);
                                bytesRead = is.read(aByte);
                        } while (bytesRead != -1);

                        bos.write(baos.toByteArray());
                        bos.flush();
                        bos.close();
                        clientSocket.close();

                    }catch (IOException ex) {
                    System.out.println("Agora deu um ruim na passagem dos dados");
                  }
          }
        }
      }catch (FileNotFoundException e) {
        e.printStackTrace();
      }catch (IOException e) {
        e.printStackTrace();

        }
    }
}

// NOTE: WASTELAND
// DatagramSocket clientSocket = new DatagramSocket();
// InetAddress IPAddress = InetAddress.getByName(lines[i]);
// byte[] sendData = new byte[65507];
// byte[] receiveData = new byte[65507];
// sendData = sentence.getBytes();
// System.out.println(sendData.length);
// DatagramPacket sendPacket
// = new DatagramPacket(sendData, sendData.length,
// IPAddress, 9876);
// clientSocket.send(sendPacket);
// DatagramPacket receivePacket
// = new DatagramPacket(receiveData,
// receiveData.length);
// clientSocket.receive(receivePacket);
// String modifiedSentence
// = new String(receivePacket.getData());
// System.out.println("FROM SERVER:"
// + modifiedSentence);
// clientSocket.close();
