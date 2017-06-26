import java.io.*;
import java.util.ArrayList;

class reconstrucao{
    public static void main(String args[]) throws Exception {
        FileInputStream file = null;
        Metadado meta = new Metadado();

        if(args.length == 0){
            System.out.println("Uso: reconstrucao [metadado]");
            System.exit(0);
        }
        file = new FileInputStream(args[0]);//checar .sdi?
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
            chunk = new FileInputStream(String.valueOf("chunks/"+it.hash_chunk)+".chunk");
            long size = chunk.getChannel().size();
            byte[] buffer = new byte[65507];
            chunk.read(buffer, 0, (int)size);
            out.write(buffer, 0, (int)size);
            chunk.close();
        }
        out.close();
    }
}
