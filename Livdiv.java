package splitFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Livdiv {
	private static final String SPLIT_OPTION = "-b 1MB";
	private ArrayList<String> div_part_list = new ArrayList<String>();

	// div_convert file
	public void div_convert(String src){
		String dst = div_name(src);
		File file = new File(dst);

		if(! file.isDirectory()){
			div_create(dst);
		}

		//TODO
	}

	// div_name file
	public String div_name(String fileName){
		Pattern p = Pattern.compile("\\.div$|\\.div/$");
		Matcher m = p.matcher(fileName);

		if(m.find()){
			String name = undiv_name(fileName) + ".div";
			return name;
		}else{
			return fileName +".div";
		}
	}

	// undiv_name file.div
	public String undiv_name(String fileName){
		String splitLastName = new File(fileName).getName();
		String[] unDivName = splitLastName.split(".div");
		return unDivName[unDivName.length-1];
	}

	// div_create file.dir
	public void div_create(String dst){
		File file = new File(dst);
		File linked = new File(dst + "/linked");
		File sha1 = new File(dst + "/sha1");

		if(! file.isDirectory()){
			file.mkdirs();
			div_create_ls_l(div_name(dst));
			try {
				linked.createNewFile();
				sha1.createNewFile();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

	public void div_create_ls_l(String dst){
		//touch
		File file = new File(dst +"/" + dst);

		try {
			file.createNewFile();
		} catch (IOException e) {
			System.out.println(e);
		}

		// ls -l >ls-l
		File lsl = new File(dst + "/ls-l");

		try {
			lsl.createNewFile();
		} catch (IOException e) {
			System.out.println(e);
		}

		try {
			FileWriter filewriter = new FileWriter(lsl);

			if(file.canRead()){
				filewriter.write("r");
			}else{
				filewriter.write("-");
			}

			if(file.canWrite()){
				filewriter.write("w");
			}else{
				filewriter.write("-");
			}

			if(file.canExecute()){
				filewriter.write("x");
			}else{
				filewriter.write("-");
			}

			filewriter.close();
		} catch (IOException e) {
			System.out.println(e);;
		}

		file.delete();
	}

	// TODO
	// div_write file.dir
	public void div_write(String s){
		String dst = follow_link(s);
		File file = new File(dst);

		if(! file.isDirectory()){
			div_create(dst);
		}else{
			div_trunc(dst);
		}
	}

	// follow_link
	public String follow_link(String s){
		String src = s + "/link";

		while(new File(src).isFile()){
			try{
				FileReader f = new FileReader(src);
				BufferedReader b = new BufferedReader(f);
				String cat;
				while(((cat = b.readLine())!=null)){
					src = cat;
				}
				b.close();
			}catch(Exception e){
				System.out.println(e);
			}
		}
		return src;
	}

	// TODO
	// div_trunk
	public void div_trunc(String s){
		String dst = follow_link(s);
		div_part(dst);
		for(int i = 0; i < div_part_list.size(); i++){
			
		}
	}

	// div_part
	public void div_part(String s){
		String src = null, dst = follow_link(s);
		File file = new File(dst);
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
		div_part_list.clear();

		for(int i = 0; i < list.size(); i++){
			Matcher mref = pref.matcher(list.get(i));

			if(mref.find()){
				String d = file.getParent();
				
				// cat
				try{
					FileReader f = new FileReader(d + "/refer");
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
				String bn = new File(list.get(i)).getName();
				String[] unDivName = bn.split(".ref");
				div_part_list.add(src + "/" + unDivName[unDivName.length-1]);
			}else{
				div_part_list.add(list.get(i));
			}
		}
	}
}
