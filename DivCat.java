package splitFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DivCat {
	public static void main(String[] args) {
		ArrayList<String> arg = new ArrayList<String>();
		ArrayList<String> option = new ArrayList<String>();
		int ch = -1;
		Libdiv livdiv = new Libdiv();
		UnixCommand cmd = new UnixCommand();
		Pattern p = Pattern.compile("^-.$");

		for(int i = 0; i < args.length; i++){
			Matcher m = p.matcher(args[i]);
			try {
				if(args[i].equals("--help") || args[i].equals("--version")){
					BufferedReader br = new BufferedReader(new FileReader(new File(args[i])));
					while((ch = br.read()) != -1){
						System.out.println((char)ch);
					}
					br.close();
					return;
				}else if(args[i].equals("-")){
					arg.add(args[i]);
				}else if(m.find()){
					option.add(args[i]);
				}else{
					arg.add(args[i]);
				}
			}catch (FileNotFoundException e) {
				System.out.println(e);
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		if(arg.isEmpty()){
			cmd.cat(option);
		}else{
			cmd.cat(option, livdiv.divConcat(arg));
		}
	}
}
