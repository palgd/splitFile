package splitFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Livdiv {
	private static final String SPLIT_OPTION = "-b 1MB";
	private ArrayList<String> div_parts_list = new ArrayList<String>();

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
		div_parts(dst);

		for(int i = 0; i < div_parts_list.size(); i++){
			File file = new File(div_parts_list.get(i));

			if(file.isFile()){
				file.delete();
			}
		}
		div_change_size(dst);
		div_change_mtime_now(dst);
	}

	// div_part
	public void div_parts(String s){
		String src = "", dst = follow_link(s);
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
		div_parts_list.clear();

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
				div_parts_list.add(src + "/" + unDivName[unDivName.length-1]);
			}else{
				div_parts_list.add(list.get(i));
			}
		}
	}

	// TODO
	// div_change_size
	public void div_change_size(String s){
		String dst = follow_link(s);
		int size = div_size(dst);
		div_replace_ls_l(dst,4,size);
	}

	// div_size and div_count
	public int div_size(String s){
		int size = 0;

		div_parts(s);

		for(int i = 0; i < div_parts_list.size();i++){
			File file = new File(div_parts_list.get(i));
			size = size + (int)file.length();
		}

		return size;
	}

	//TODO
	// div_replace_ls_l
	public void div_replace_ls_l(String dst, int n, int val){
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		String vmName = bean.getName();
		long pid = Long.valueOf(vmName.split("@")[0]);
		String tmp = dst+"div_replace_ls_l."+pid;

		File file = new File(tmp);
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			pw.write(div_substr_ls_l(dst,n,val));
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	//TODO
	// div_substr_ls_l
	public String div_substr_ls_l(String src, int n, int val){
		return "";
	}

	// TODO
	// div_change_mtime_now
	public void div_change_mtime_now(String s){

	}

}
