package splitFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnDiv {
	public static void main(String[] args) {
		DividedDataController dividedDataController = new DividedDataController();
		Pattern p = Pattern.compile("\\.div$|\\.div/$");
		Matcher m;
		
		for(int i = 0; i < args.length; i++){
			m = p.matcher(args[i]);
			if(m.find()){
				dividedDataController.combinData(args[i]);
			}
		}
	}
 }
