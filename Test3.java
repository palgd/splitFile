package splitFile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Test3 {
	public static void main(String[] args) {
		String src = "";
		
		try{
			FileReader f = new FileReader(args[0]);
			BufferedReader b = new BufferedReader(f);
			String cat;
			while(((cat = b.readLine())!=null)){
				src = cat;
			}
			b.close();
		}catch(FileNotFoundException e){
			System.out.println(e);
		}catch(IOException e){
			System.out.println(e);
		}
		
		// basename
		System.out.println(src);
	}
}
