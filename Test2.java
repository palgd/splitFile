package splitFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test2 {
	public static void main(String[] args) {
		File file = new File(args[0]);
		String[] fileName = file.list();
		ArrayList<String> list = new ArrayList<String>();
		Pattern pm = Pattern.compile("^M");
		Pattern pref = Pattern.compile("$ref");

		for(int i = 0; i < fileName.length; i++){
			Matcher mm = pm.matcher(fileName[i]);

			if(mm.find()){
				list.add(fileName[i]);
			}
		}

		Collections.sort(list);
		
		for(int i = 0; i < list.size(); i++){
			Matcher mref = pref.matcher(fileName[i]);

			if(mref.find()){
				String d = "";
			}
		}
	}
}
