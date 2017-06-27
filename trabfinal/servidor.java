
import java.io.*;
import java.net.*;

class servidor {
private final static int serverPort = 3248;
    public static void main(String args[]) throws Exception {
      try{
        ServerSocket servidor = new ServerSocket(serverPort);
        System.out.println("Servidor ouvindo a porta: "+serverPort);
        while(true) {
          Socket cliente = servidor.accept();
          BufferedReader msg = new BufferedReader( new InputStreamReader(cliente.getInputStream()));
          String teste = "";
          if(msg.ready()){
            System.out.println("Teste");
            teste = msg.readLine();
          }
          if(msg.equals("send")){
            System.out.println("opa, vamo que vamo");
          }else{
            System.out.println("quase la"+teste);
          }
          System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
          ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
          saida.flush();
          saida.close();
          cliente.close();
        }
      }
    catch(Exception e) {
       System.out.println("Erro: " + e.getMessage());
    }
  }
}
