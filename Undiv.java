package splitFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnDiv {
	public static void main(String[] args) {
		Libdiv livdiv = new Libdiv();
		Pattern p = Pattern.compile("\\.div$|\\.div/$");
		
		for(int i = 0; i < args.length; i++){
			Matcher m = p.matcher(args[i]);
			if(m.find()){
				livdiv.divRestore(args[i]);
			}
		}
	}
}
