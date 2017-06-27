
import java.io.*;
import java.net.*;

class servidor {
private final static int serverPort = 3248;
    public static void main(String args[]) throws Exception {
      try{
        byte[] aByte = new byte[1];
        int bytesRead;
        InputStream is = null;
        ServerSocket servidor = new ServerSocket(serverPort);
        System.out.println("Servidor ouvindo a porta: "+serverPort);
        while(true) {
          Socket cliente = servidor.accept();
          BufferedReader docliente = new BufferedReader( new InputStreamReader(cliente.getInputStream()));
          String mensage = docliente.readLine();
          String msg[] = mensage.split("_");
          if(msg[0].equals("send")){
            // try {
              // cliente = servidor.accept();
              is = cliente.getInputStream();
            // }catch (IOException ex) {
            //   System.out.println("Algum problema na segunda fase de conexão.");
            // }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.out.println(msg[1]);
            FileOutputStream fos = new FileOutputStream("chunkspassed/"+msg[1]+".chunk");
            System.out.println(msg[1]);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bytesRead = is.read(aByte, 0, aByte.length);
            do {
              baos.write(aByte);
              bytesRead = is.read(aByte);
            } while (bytesRead != -1);
            bos.write(baos.toByteArray());
            bos.flush();
            bos.close();
            cliente.close();
          }else{
            System.out.println("quase la! "+msg);

          }
          System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
          // ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
          // saida.flush();
          // saida.close();
          // cliente.close();
        }
      }
    catch(Exception e) {
       System.out.println("Erro: " + e.getMessage());
    }
  }
}
