package splitFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
	// divConvert file
	public void divConvert(String src){
		String dst = divName(src);
		File file = new File(dst);

		if(! file.isDirectory()){
			divCreate(dst);
		}

	}

	// divName file
	public String divName(String fileName){
		Pattern p = Pattern.compile("\\.div$|\\.div/$");
		Matcher m = p.matcher(fileName);

		if(m.find()){
			String name = undivName(fileName) + ".div";
			return name;
		}else{
			return fileName +".div";
		}
	}

	// undivName file.div
	public String undivName(String fileName){
		String splitLastName = new File(fileName).getName();
		String[] unDivName = splitLastName.split(".div");
		return unDivName[unDivName.length-1];
	}

	// divCreate file.dir
	public void divCreate(String dst){
		File file = new File(dst);
		File linked = new File(dst + "/linked");
		File sha1 = new File(dst + "/sha1");
		File lsl = new File(dst+"ls-l");

		if(! file.isDirectory()){
			file.mkdirs();
			divCreateLsL(divName(dst));
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

	// divCreateLsL
	public void divCreateLsL(String dst){
		File file = new File(dst +"/" + dst);
		File lsl = new File(dst + "/ls-l");
		Calendar cal = Calendar.getInstance();
		String[] lsL = lsl(file);

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
			for(int i = 0; i < lsL.length; i++){
				fw.write(lsL[i]+" ");
			}
			fw.close();
		} catch (IOException e) {
			System.out.println(e);
		}

		file.delete();
	}

	// TODO
	// divWrite file.dir
	public void divWrite(String s, File src){
		String dst = followLink(s);
		File file = new File(dst);

		if(! file.isDirectory()){
			divCreate(dst);
		}else{
			divTrunc(dst);
		}

	}

	// followLink
	public String followLink(String s){
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

	// divTrunk
	public void divTrunc(String s){
		String dst = followLink(s);
		ArrayList<String> divPartsList = divParts(dst);

		for(int i = 0; i < divPartsList.size(); i++){
			File file = new File(divPartsList.get(i));

			if(file.isFile()){
				file.delete();
			}
		}
		divChangeSize(dst);
		divChangeMtimeNow(dst);
	}

	// divPart
	public ArrayList<String> divParts(String s){
		String src = "", dst = followLink(s);
		File file = new File(dst);
		String[] fileName = file.list();
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> divPartsList = new ArrayList<String>();
		Pattern pm = Pattern.compile("^M");
		Pattern pref = Pattern.compile("$ref");

		for(int i = 0; i < fileName.length; i++){
			Matcher mm = pm.matcher(fileName[i]);

			if(mm.find()){
				list.add(fileName[i]);
			}
		}

		Collections.sort(list);
		divPartsList.clear();

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
				divPartsList.add(src + "/" + unDivName[unDivName.length-1]);
			}else{
				divPartsList.add(list.get(i));
			}
		}
		return divPartsList;
	}

	// divChangeSize
	public void divChangeSize(String s){
		String dst = followLink(s);
		int size = divSize(dst);
		divReplaceLsL(dst,1,size);
	}

	// divSize and divCount
	public int divSize(String s){
		int size = 0;

		ArrayList<String> divPartsList = divParts(s);

		for(int i = 0; i < divPartsList.size();i++){
			File file = new File(divPartsList.get(i));
			size = size + (int)file.length();
		}

		return size;
	}

	// divReplaceLsL
	public void divReplaceLsL(String dst, int n, int val){
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		String vmName = bean.getName();
		long pid = Long.valueOf(vmName.split("@")[0]);
		String tmp = dst+"divReplaceLsL."+pid;
		String[] sa = divSubstrLsL(dst,n,val);
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

	// divSubstrLsL
	public String[] divSubstrLsL(String src, int n, int val){
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

	// divChangeMtimeNow
	public void divChangeMtimeNow(String s){
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		String vmName = bean.getName();
		long pid = Long.valueOf(vmName.split("@")[0]);
		String dst = followLink(s);
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

		divChangeMtime(dst,now);
		file.delete();
	}

	// divChangeMtime
	public void divChangeMtime(String s,String last){
		String dst = followLink(s);
		File file = new File(last);
		String[] lsL = lsl(file);

		divReplaceLsL(dst, 2, Integer.valueOf(lsL[2]));
		divReplaceLsL(dst, 3, Integer.valueOf(lsL[3]));
	}

	/*
	public int[] getLastModified(File file){
		Calendar cal = Calendar.getInstance();
		Date date = new Date(file.lastModified());
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OFDAY);
		int minute = cal.get(Calendar.MINUTE);
		int[] lastModified ={month,day,hour,minute};
		return lastModified;
	}*/

	// ls -l
	public String[] lsl(File file){
		String[] lsL = new String[5];
		Date date = new Date(file.lastModified());
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);

		if(file.canRead()){
			lsL[0] = "r";
		}else{
			lsL[0] = "-";
		}
		if(file.canWrite()){
			lsL [0] += "w";
		}else{
			lsL [0] += "-";
		}
		if(file.canExecute()){
			lsL [0] += "x";
		}else{
			lsL [0] += "-";
		}
		lsL[1] = String.valueOf(file.length());
		lsL[2] = String.valueOf(month);
		lsL[3] = String.valueOf(day);
		lsL[4] = String.valueOf(hour) + ":" + String.valueOf(minute);
		lsL[5] = file.getName();

		return lsL;
	}

	// split
	public void split(String outDir, String inFile){
		int i = 1, size = 0;
		boolean isFlag = true;
		File od = new File(outDir);

		if(! od.exists()){
			od.mkdir();
		}

		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inFile));
			BufferedOutputStream bos = null;
			byte[] b = new byte[1024];

			while((bis.read(b)) != -1){
				if(size >= 1024 || isFlag){
					File outFile = new File(outDir + "/M" + String.format("%018d", i++));
					outFile.createNewFile();
					bos = new BufferedOutputStream(new FileOutputStream(outFile));
					isFlag = false;
				}
				bos.write(b);
				size++;
			}

			bis.close();
			bos.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

	}
}
