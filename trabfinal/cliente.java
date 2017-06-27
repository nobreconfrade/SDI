import java.io.*;
import java.net.*;
import java.util.ArrayList;

class cliente {
  private final static int serverPort = 3248;

    public static void main(String args[]) throws Exception {
        FileInputStream listaips,file = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        String sentence = "";
        Metadado meta = new Metadado();
        file = new FileInputStream(args[0]);
        try {

            byte[] bytes = new byte[(int)file.getChannel().size()];
            file.read(bytes);
            ByteArrayInputStream lbos = new ByteArrayInputStream(bytes);
            ObjectInput in   = new ObjectInputStream(lbos);
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

            for(int i=0;i<meta.n_chunks;i++){
                Socket server = new Socket (lines[i%lines.length],serverPort);
                DataOutputStream paraservidor = new DataOutputStream(server.getOutputStream());
                paraservidor.writeBytes("send");
                paraservidor.flush();
                paraservidor.close();
            }
      }catch (FileNotFoundException e) {
        e.printStackTrace();
      }catch (IOException e) {
        e.printStackTrace();
      }
  }
}


            // for(int i=0;i<meta.n_chunks;i++){
              // ServerSocket welcomeSocket = null;
              // Socket connectionSocket = null;
              // BufferedOutputStream outToClient = null;
              //
              //  try {
              //      welcomeSocket = new ServerSocket(3248);
              //      connectionSocket = welcomeSocket.accept();
              //      outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());
              //  } catch (IOException ex) {
              //      // Do exception handling
              //  }
              //
              //  if (outToClient != null) {
              //      File myFile = new File("chunks/"+meta.vetor_dados.get(i).hash_chunk+".chunk");
              //      byte[] mybytearray = new byte[(int) myFile.length()];
              //
              //      FileInputStream fis = null;
              //
              //      try {
              //          fis = new FileInputStream(myFile);
              //      } catch (FileNotFoundException ex) {
              //          System.out.println("Não encontrou o chunk na maquina local");
              //      }
              //      BufferedInputStream bis = new BufferedInputStream(fis);
              //
              //      try {
              //          bis.read(mybytearray, 0, mybytearray.length);
              //          outToClient.write(mybytearray, 0, mybytearray.length);
              //          outToClient.flush();
              //          outToClient.close();
              //          connectionSocket.close();
              //
              //          // File sent, exit the main method
              //          return;
              //      } catch (IOException ex) {
              //          System.out.println("Não conseguiu enviar o arquivo");
              //      }
// TODO: Recebimento abaixo
