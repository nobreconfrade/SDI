import java.security.*;
import java.io.*;

class testedigest{
    public static void main(String[] args) throws Exception{
        try{
            String yourString = "huehue";
            byte[] bytesOfMessage = yourString.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesOfMessage);
            String str2 = new String(thedigest, "UTF-8");
            System.out.println(str2);
        }catch(Exception e){
          return ;
        }
    }
}
