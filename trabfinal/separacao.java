import java.io.*;

class test{
    public static void main(String args[]) throws Exception {
        FileInputStream in = null;
        String path[] = args[0].split("/");
        String nome = path[path.length - 1];
        // System.out.println(nome);
		in = new FileInputStream(args[0]);
		byte[] buffer = new byte[65507];
		int count = 0;
		long size = in.getChannel().size();
    	int i = 0, len = 0;
        while(true){
        	count += in.read(buffer, 0, len = (int)(count+65507 < size ? 65507 : size-count));
        	if(len == 0)
        		break;
			FileOutputStream out = new FileOutputStream("output"+i+".jpg");
        	out.write(buffer, 0, len);
        	out.close();
        	i++;
            if(len < 65507)
                break;
        }
        FileOutputStream meta = new FileOutputStream(nome+".sdi");
        PrintStream printStream = new PrintStream(meta);
        printStream.println(nome);
        meta.write(i);
        meta.close();
        in.close();
	}
}
