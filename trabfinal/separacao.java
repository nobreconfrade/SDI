import java.io.*;
import java.util.ArrayList;

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
        for(Dados it: list)
            System.out.println(it.hash_chunk);
    }
}
class separacao{
    public static void main(String args[]) throws Exception {
        FileInputStream in = null;
        if(args.length == 0){
            System.out.println("Uso: separacao [arquivo]");
            System.exit(0);
        }
        String path[] = args[0].split("/");
        String nome = path[path.length-1];
        // System.out.println(nome);
        in = new FileInputStream(args[0]);
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
            aux.vetor_bd_chunk.add("teste");
            meta.vetor_dados.add(aux);
            // System.out.println(hashvalue);
            FileOutputStream out = new FileOutputStream("chunks/" + String.valueOf(hashvalue) + ".chunk");
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
        meta.debug();
        in.close();
    }
}
