package splitFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) {
		Pattern p = Pattern.compile("^-.$");
		Matcher m = p.matcher(args[0]);
		if(m.find()) System.out.println("ok");
	}
}
