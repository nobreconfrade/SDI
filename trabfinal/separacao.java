import java.io.*;
import java.util.ArrayList;

class Dados{
    int     hash_chunk;

    ArrayList <String>  vetor_bd_chunk = new ArrayList <Dados>();
}
class Metadado{
    String  nome;
    long    size;
    int     n_chunks;

    ArrayList <Dados>   vetor_dados = new ArrayList <Dados>();

    public void save() throws Exception{
        FileOutputStream file = new FileOutputStream(this.nome+".sdi");
        PrintStream printStream = new PrintStream(file);
        printStream.println(this.nome);
        printStream.println(this.size);
        file.write(this.n_chunks);
        file.close();
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
            meta.vetor_dados.add(aux);
            // System.out.println(hashvalue);
            FileOutputStream out = new FileOutputStream(String.valueOf(hashvalue));
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

        in.close();
	}
}
