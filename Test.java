package splitFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Test {
	public static void main(String[] args) {
		String s = "st";
		ArrayList<String> list = grep(s,args[0]);
		for(int i = 0; i < list.size(); i++){
			System.out.println(list.get(i));
		}
	}
	
	public static ArrayList<String> grep(String pattern, String fileName){
		ArrayList<String> list = new ArrayList<String>();
		String line = "";
		File file = new File(fileName);
		BufferedReader br;
		
		try {
			br = new BufferedReader(new FileReader(file));
			Pattern pm = Pattern.compile(pattern);
			
			while((line = br.readLine()) != null){
				Matcher mm = pm.matcher(line);
				if(mm.find()){
					list.add(line);
				}
			}
			br.close();
		}catch(FileNotFoundException e){
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		return list;
	}
}
