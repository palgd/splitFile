package splitFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Undiv {
	public static void main(String[] args) {
		Livdiv livdiv = new Livdiv();
		Pattern p = Pattern.compile("\\.div$|\\.div/$");
		
		for(int i = 0; i < args.length; i++){
			Matcher m = p.matcher(args[i]);
			if(m.find()){
				livdiv.divRestore(args[i]);
			}
		}
	}
}
