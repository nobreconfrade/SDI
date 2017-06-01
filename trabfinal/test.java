import java.io.*;

class test{
    public static void main(String args[]) throws Exception {
        FileInputStream in = null;
		in = new FileInputStream("tests/test1.jpg");
		
		// FILE *file = fopen("tests/test1.jpg");
		// FILE *fout = fopen("output.jpg");

		byte[] buffer = new byte[65507];
		int count = 0;
		long size = in.getChannel().size();
		while ((count += in.read(buffer, 0, 65507)) < size){
			int i = 0;
			FileOutputStream out = new FileOutputStream("output"+i+".jpg");
        	out.write(buffer);
        	out.close();
        	i++;
        }
        in.close();
	}
}