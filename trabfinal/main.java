import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.*;

class Dados implements Serializable{
    int     hash_chunk;

    ArrayList <String>  vetor_bd_chunk = new ArrayList <String>();
}

class Metadado implements Serializable{
    String  nome;
    long    size;
    int     n_chunks;

    ArrayList <Dados>   vetor_dados = new ArrayList <Dados>();

    public void save() throws Exception{
        FileOutputStream file = new FileOutputStream(this.nome+".sdi");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);

        out.writeUTF(this.nome);
        out.writeLong(this.size);
        out.writeInt(this.n_chunks);
        out.writeObject(this.vetor_dados);
        byte[] buf = bos.toByteArray();
        file.write(buf);
        file.close();
    }

    public void debug() throws Exception{
        FileInputStream file = new FileInputStream(this.nome+".sdi");
        byte[] bytes = new byte[(int)file.getChannel().size()];
        file.read(bytes);
        ByteArrayInputStream bos = new ByteArrayInputStream(bytes);
        ObjectInput in = new ObjectInputStream(bos);
        String nome = in.readUTF();
        System.out.println(nome);
        long size = in.readLong();
        System.out.println(size);
        int n_chunks = in.readInt();
        System.out.println(n_chunks);
        ArrayList <Dados> list = new ArrayList <Dados>();
        // in.readObject(list);
        list = (ArrayList <Dados>) in.readObject();
        for(Dados it: list){
            System.out.println("Hash = "+it.hash_chunk);
            System.out.println("Endereços:");
            for(String ip: it.vetor_bd_chunk)
                System.out.println(ip);
        }
    }
}

class main{
    public static String separacao(String args) throws Exception {
        FileInputStream in = null;
        // if(args.length == 0){
        //     System.out.println("Uso: separacao [arquivo]");
        //     System.exit(0);
        // }
        String path[] = args.split("/");
        String nome = path[path.length-1];
        // System.out.println(nome);
        in = new FileInputStream(args);
        byte[] buffer = new byte[65507];
        int count = 0;
        long size = in.getChannel().size();
        int i = 0, len = 0;
        Metadado meta   = new Metadado();
        while(true){
            count += in.read(buffer, 0, len = (int)(count+65507 < size ? 65507 : size-count));
            if(len == 0)
                break;
            String hashstring = new String(buffer, "UTF-8");
            int hashvalue = hashstring.hashCode();
            if (hashvalue < 0){
              hashvalue *= -1;
            }
            Dados aux = new Dados();
            aux.hash_chunk = hashvalue;
            // aux.vetor_bd_chunk.add("teste");
            meta.vetor_dados.add(aux);
            // System.out.println(hashvalue);
            FileOutputStream out = new FileOutputStream("chunks/" + hashvalue + ".chunk");
            out.write(buffer, 0, len);
            out.close();
            i++;
            if(len < 65507)
                break;
        }
        meta.nome       = nome;
        meta.size       = size;
        meta.n_chunks   = i;
        meta.save();
        // meta.debug();
        in.close();
        return nome+".sdi";
    }
    public static void reconstrucao(String args) throws Exception {
        FileInputStream file = null;
        Metadado meta = new Metadado();

        // if(args.length == 0){
        //     System.out.println("Uso: reconstrucao [metadado]");
        //     System.exit(0);
        // }
        file = new FileInputStream(args);//checar .sdi?
        //leitura
        byte[] bytes = new byte[(int)file.getChannel().size()];
        file.read(bytes);
        ByteArrayInputStream bos = new ByteArrayInputStream(bytes);
        ObjectInput in  = new ObjectInputStream(bos);
        meta.nome       = in.readUTF();
        meta.size       = in.readLong();
        meta.n_chunks   = in.readInt();
        meta.vetor_dados = (ArrayList <Dados>) in.readObject();
        //fimleitura
        FileOutputStream out = new FileOutputStream("reconstruido.jpg");
        FileInputStream chunk;
        for(Dados it: meta.vetor_dados){
            chunk = new FileInputStream("chunks/"+it.hash_chunk+".chunk");
            long size = chunk.getChannel().size();
            byte[] buffer = new byte[65507];
            chunk.read(buffer, 0, (int)size);
            out.write(buffer, 0, (int)size);
            chunk.close();
        }
        out.close();
    }
    public static void cliente(String arquivosdi) throws Exception {
        int serverPort = 3248;
        FileInputStream listaips,file = null;
        DataInputStream dis = null;
        String sentence = "";
        Metadado meta = new Metadado();
        file = new FileInputStream(arquivosdi);
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
                // Socket server = new Socket (lines[i%lines.length],serverPort);
                Socket server = new Socket ("localhost",serverPort);
                DataOutputStream paraservidor = new DataOutputStream(server.getOutputStream());
                paraservidor.writeBytes("send_"+String.valueOf(meta.vetor_dados.get(enviados).hash_chunk));
                System.out.println("send_"+meta.vetor_dados.get(enviados).hash_chunk);
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

            meta.debug();
            return;
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void servidor() throws Exception {
        int serverPort = 3248;
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
                System.out.println(msg[1]);
                msg[1] = msg[1].replaceAll("(\\d+).*", "$1");
                System.out.println(msg[1]);
                // try {
                // cliente = servidor.accept();
                is = cliente.getInputStream();
                // }catch (IOException ex) {
                //   System.out.println("Algum problema na segunda fase de conexão.");
                // }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                FileOutputStream fos = new FileOutputStream("chunkspassed/"+msg[1]+".chunk");
                // FileOutputStream fos = new FileOutputStream("chunkspassed/BABANA.chunk");
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bytesRead = is.read(aByte, 0, aByte.length);
                do {
                    baos.write(aByte);
                    bytesRead = is.read(aByte);
                }while (bytesRead != -1);
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
    
    public static void main(String args[]) throws Exception {
        System.out.println("Selecione a operação:");
        System.out.println("1 - Separação [arquivo.jpg]");
        System.out.println("2 - Recuperação [metadado.sdi]");
        Scanner s = new Scanner(System.in);

        int     option  = 0;
        String  arquivo = "";
        if (s.hasNextInt())
            option = s.nextInt();
        else{
            System.out.println("Opção vazia");
            System.exit(0);
        }

        if (s.hasNextLine())
            arquivo = s.nextLine().trim();
        else{
            System.out.println("Sem arquivo");
            System.exit(0);
        }
        // System.out.println(arquivo);

        if (option == 1) {
            String arquivosdi = separacao(arquivo);
            cliente(arquivosdi);
        }
        else if (option == 2){
            reconstrucao(arquivo);
        }
        else{
            System.out.println("Opção inválida");
            System.exit(0);
        }
    }
}