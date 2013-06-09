package splitFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Livdiv {
	private static final String SPLIT_OPTION = "-b 1MB";

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
		String[] splitName = fileName.split("/");
		String splitLastName = splitName[splitName.length-1];
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
				System.out.println("ÉtÉ@ÉCÉãì«Ç›çûÇ›é∏îs");
			}
		}
		return src;
	}
	
	// TODO
	public void div_trunc(String s){
		String dst = follow_link(s);
		div_part(dst);
	}
	
	
	public void div_part(String s){
		String dst = follow_link(s);
	}
}
