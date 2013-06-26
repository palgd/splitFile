package splitFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class DivCat {
	public static void main(String[] args) {
		String arg = "", options = "";
		int ch = 0;
		Livdiv livdiv = new Livdiv();
		Pattern p = Pattern.compile("-.");

		for(int i = 0; i < args.length; i++){
			try {
				BufferedReader br = new BufferedReader(new FileReader(new File(args[i])));
				if(args[i].equals("--help") || args[i].equals("--version")){
					while((ch = br.read()) != -1){
						System.out.println((char)(ch));
					}
					br.close();
					return;
				}else if(args[i].equals("-")){
					
				}else if(true){
					
				}
				br.close();
			}catch (FileNotFoundException e) {
				System.out.println(e);
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
}
