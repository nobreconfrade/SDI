import java.io.*;

class reconstrucao{
    public static void main(String args[]) throws Exception {
    	int chunks = 3;
    	FileOutputStream out = new FileOutputStream("reconstruido.jpg");
    	for(int i=0; i<chunks; i++){
        	FileInputStream in = new FileInputStream("output"+i+".jpg");
        	long size = in.getChannel().size();
			byte[] buffer = new byte[65507];
			in.read(buffer, 0, (int)size);
			out.write(buffer, 0, (int)size);
        	in.close();
    	}
    	out.close();
    }
}