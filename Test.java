package splitFile;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Test {
	public static void main(String[] args) {
		int len=0;
		byte[] b = new byte[1026];

		try{
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(args[0]));
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			
			while((len=bis.read(b)) != -1){
				md.update(b,0,len);
			}
			bis.close();
			
			byte[] sha1 = md.digest();
			System.out.print("SHA1(test.txt)= ");
			for(int i = 0; i < sha1.length; i++){
				if(sha1[i] < 16){
					System.out.print("0"+Integer.toHexString(sha1[i] & 0xff));
				}else{
					System.out.print(Integer.toHexString(sha1[i] & 0xff));
				}
			}
			System.out.println();

		}catch(NoSuchAlgorithmException e){
			System.out.println(e);
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
