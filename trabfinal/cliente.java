import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

class cliente {
  private final static int serverPort = 3248;
  static Scanner s = new Scanner(System.in);
  static String escolha;

    public static void main(String args[]) throws Exception {
      System.out.print("Escolha entre [separacao] ou [reconstrucao]:");
      escolha = s.nextLine().trim();

      if(escolha.equals("separacao")){
        FileInputStream listaips,file = null;
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
          int enviados = 0;
          // for(int i=0; i<lines.length && enviados<meta.n_chunks;i++){
          for(int i=0;enviados<meta.n_chunks;i++){
            InetAddress teste = InetAddress.getByName(lines[i%lines.length]);
            if(!teste.isReachable(1000)){
              System.out.println("Conexão falhou");
              continue;
            }
            Socket server = new Socket (lines[i%lines.length],serverPort);
            // Socket server = new Socket ("localhost",serverPort);
            DataOutputStream paraservidor = new DataOutputStream(server.getOutputStream());
            paraservidor.writeBytes("send_"+String.valueOf(meta.vetor_dados.get(enviados).hash_chunk));
            // System.out.println("send_"+meta.vetor_dados.get(enviados).hash_chunk);
            paraservidor.flush();
            // paraservidor.close();
            // server.close();
            // server = new Socket (lines[i%lines.length],serverPort);
            BufferedOutputStream bufferservidor = new BufferedOutputStream(server.getOutputStream());
            File meuchunk = new File("chunks/"+meta.vetor_dados.get(enviados).hash_chunk+".chunk");
            byte[] mybytearray = new byte[(int) meuchunk.length()];
            FileInputStream fis = new FileInputStream(meuchunk);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(mybytearray, 0, mybytearray.length);
            bufferservidor.write(mybytearray, 0, mybytearray.length);
            bufferservidor.flush();
            bufferservidor.close();
            server.close();
            meta.vetor_dados.get(enviados).vetor_bd_chunk.add(lines[i%lines.length]);
            meta.save();
            // System.out.println(meta.vetor_dados.get(enviados).vetor_bd_chunk.get(0));
            enviados++;
          }
          if(enviados != meta.n_chunks)
          System.out.println("Envio falhou");
          else{
            System.out.println("Sucesso");
            for(int i=0;i<meta.n_chunks;i++){
              File filel = new File("chunks/"+meta.vetor_dados.get(i).hash_chunk+".chunk");
              if (filel.delete()) {
                System.out.println(filel.getName() + " foi deletada!");
              }
            }
          }
          // meta.debug();
      }catch (FileNotFoundException e) {
        e.printStackTrace();
      }catch (IOException e) {
        e.printStackTrace();
      }
  }else{
    FileInputStream listaips,file = null;
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

      for (int i=0;i<meta.n_chunks;i++) {
        InetAddress teste = InetAddress.getByName(meta.vetor_dados.get(i%lines.length).vetor_bd_chunk.get(0));
        if(!teste.isReachable(1000)){
          System.out.println("Conexão falhou");
          continue;
        }
        byte[] aByte = new byte[1];
        int bytesRead;
        InputStream is = null;
        Socket server = new Socket (meta.vetor_dados.get(i%lines.length).vetor_bd_chunk.get(0),serverPort);
        DataOutputStream paraservidor = new DataOutputStream(server.getOutputStream());
        paraservidor.writeBytes("_"+String.valueOf(meta.vetor_dados.get(i%lines.length).hash_chunk));
        paraservidor.flush();
        is = server.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fos = new FileOutputStream("chunks/"+String.valueOf(meta.vetor_dados.get(i%lines.length).hash_chunk)+".chunk");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        System.out.println(i);
        bytesRead = is.read(aByte, 0, aByte.length);
        do {
          baos.write(aByte);
          bytesRead = is.read(aByte);
        } while (bytesRead != -1);
        bos.write(baos.toByteArray());
        bos.flush();
        bos.close();
        server.close();
      }
      }catch (FileNotFoundException e) {
        e.printStackTrace();
      }catch (IOException e) {
        e.printStackTrace();
      }
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
