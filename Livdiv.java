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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Livdiv {
	private static final String SPLIT_OPTION = "-b 1MB";

	//TODO
	// div_convert file
	public void div_convert(String src){
		String dst = div_name(src);
		File file = new File(dst);

		if(! file.isDirectory()){
			div_create(dst);
		}

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
		File lsl = new File(dst+"ls-l");

		if(! file.isDirectory()){
			file.mkdirs();
			div_create_ls_l(div_name(dst));
			if(! linked.exists() && ! sha1.exists()){
				try {
					linked.createNewFile();
					sha1.createNewFile();
				} catch (IOException e) {
					System.out.println(e);
				}
			}else{
				linked.setLastModified(lsl.lastModified());
				sha1.setLastModified(lsl.lastModified());
			}
		}
	}

	// div_create_ls_l
	public void div_create_ls_l(String dst){
		File file = new File(dst +"/" + dst);
		File lsl = new File(dst + "/ls-l");
		Calendar cal = Calendar.getInstance();
		String[] ls_l = lsl(file);

		//touch
		if(! file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println(e);
			}
		}else{
			file.setLastModified(cal.getTimeInMillis());
		}

		// ls -l >ls-l
		try {
			lsl.createNewFile();
		} catch (IOException e) {
			System.out.println(e);
		}
		
		try {
			FileWriter fw = new FileWriter(lsl);
			for(int i = 0; i < ls_l.length; i++){
				fw.write(ls_l[i]+" ");
			}
			fw.close();
		} catch (IOException e) {
			System.out.println(e);
		}

		file.delete();
	}

	// TODO
	// div_write file.dir
	public void div_write(String s, File src){
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

	// div_trunk
	public void div_trunc(String s){
		String dst = follow_link(s);
		ArrayList<String> div_parts_list = div_parts(dst);

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
	public ArrayList<String> div_parts(String s){
		String src = "", dst = follow_link(s);
		File file = new File(dst);
		String[] fileName = file.list();
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> div_parts_list = new ArrayList<String>();
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

				try{
					FileReader f = new FileReader(d + "/refer");
					BufferedReader b = new BufferedReader(f);
					src = b.readLine();
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
		return div_parts_list;
	}

	// div_change_size
	public void div_change_size(String s){
		String dst = follow_link(s);
		int size = div_size(dst);
		div_replace_ls_l(dst,1,size);
	}

	// div_size and div_count
	public int div_size(String s){
		int size = 0;

		ArrayList<String> div_parts_list = div_parts(s);

		for(int i = 0; i < div_parts_list.size();i++){
			File file = new File(div_parts_list.get(i));
			size = size + (int)file.length();
		}

		return size;
	}

	// div_replace_ls_l
	public void div_replace_ls_l(String dst, int n, int val){
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		String vmName = bean.getName();
		long pid = Long.valueOf(vmName.split("@")[0]);
		String tmp = dst+"div_replace_ls_l."+pid;
		String[] sa = div_substr_ls_l(dst,n,val);
		File file = new File(tmp);
		File lsl = new File(dst+"/ls-l");

		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			for (int i = 0; i < sa.length; i++){
				pw.write(sa[i] + " ");
			}
			pw.close();
		} catch (IOException e) {
			System.out.println(e);
		}

		file.setLastModified(lsl.lastModified());
		file.renameTo(lsl);

	}

	// div_substr_ls_l
	public String[] div_substr_ls_l(String src, int n, int val){
		String[] sa = null;
		try {
			BufferedReader b = new BufferedReader(new FileReader(src+"ls-l"));
			sa = b.readLine().split(" ");
			sa[n] = String.valueOf(val);
			b.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		return sa;
	}

	// div_change_mtime_now
	public void div_change_mtime_now(String s){
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		String vmName = bean.getName();
		long pid = Long.valueOf(vmName.split("@")[0]);
		String dst = follow_link(s);
		String now = dst + "now."+ pid;
		File file = new File(now);
		Calendar cal = Calendar.getInstance();

		if(! file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println(e);
			}
		}else{
			file.setLastModified(cal.getTimeInMillis());
		}

		div_change_mtime(dst,now);
		file.delete();
	}

	// div_change_mtime
	public void div_change_mtime(String s,String last){
		String dst = follow_link(s);
		File file = new File(last);
		String[] ls_l = lsl(file);
		
		div_replace_ls_l(dst, 2, Integer.valueOf(ls_l[2]));
		div_replace_ls_l(dst, 3, Integer.valueOf(ls_l[3]));
	}

	/*
	public int[] getLastModified(File file){
		Calendar cal = Calendar.getInstance();
		Date date = new Date(file.lastModified());
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int[] lastModified ={month,day,hour,minute};
		return lastModified;
	}*/

	// ls -l
	public String[] lsl(File file){
		String[] ls_l = new String[5];
		Date date = new Date(file.lastModified());
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);

		if(file.canRead()){
			ls_l[0] = "r";
		}else{
			ls_l[0] = "-";
		}
		if(file.canWrite()){
			ls_l [0] += "w";
		}else{
			ls_l [0] += "-";
		}
		if(file.canExecute()){
			ls_l [0] += "x";
		}else{
			ls_l [0] += "-";
		}
		ls_l[1] = String.valueOf(file.length());
		ls_l[2] = String.valueOf(month);
		ls_l[3] = String.valueOf(day);
		ls_l[4] = String.valueOf(hour) + ":" + String.valueOf(minute);
		ls_l[5] = file.getName();
		
		return ls_l;
	}
}
