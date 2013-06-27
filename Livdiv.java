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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Livdiv {
	UnixCommand cmd = new UnixCommand();
	
	// divConvert file
	public void divConvert(String src){
		String dst = divName(src);
		File file = new File(dst);

		if(! file.isDirectory()){
			divCreate(dst);
		}
		divWrite(src,dst);
		divChangeMtime(dst,src);
	}

	// divName file
	public String divName(String fileName){
		Pattern p = Pattern.compile("\\.div$|\\.div/$");
		Matcher m = p.matcher(fileName);

		if(m.find()){
			String name = unDivName(fileName) + ".div";
			return name;
		}else{
			return fileName +".div";
		}
	}

	// undivName file.div
	public String unDivName(String fileName){
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
		String[] lsL = cmd.lsl(file);

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

	// divWrite file.dir
	public void divWrite(String src, String s){
		String dst = followLink(s);
		File file = new File(dst);

		if(! file.isDirectory()){
			divCreate(dst);
		}else{
			divTrunc(dst);
		}
		cmd.split(src,dst);
		divUpdateSha1(dst);
		divChangeSize(dst);
		divChangeMtimeLast(dst);
	}

	// followLink
	public String followLink(String src){

		while(new File(src+"/link").isFile()){
			try{
				BufferedReader b = new BufferedReader(new FileReader(src));
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

				// basename .ref
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
			BufferedReader b = new BufferedReader(new FileReader(src+"/ls-l"));
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
		String[] lsL = cmd.lsl(file);

		divReplaceLsL(dst, 2, Integer.valueOf(lsL[2]));
		divReplaceLsL(dst, 3, Integer.valueOf(lsL[3]));
	}

	//divUpdateSha1
	public void divUpdateSha1(String s){
		String dst = followLink(s);
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		String vmName = bean.getName();
		long pid = Long.valueOf(vmName.split("@")[0]);
		String tmp = dst+"/div-update-sha1"+pid;
		ArrayList<String> divPartsList = divParts(dst);
		File tmpFile = new File(tmp);
		File dstFile = new File(dst + "/sha1");

		try {
			PrintWriter bw =new PrintWriter(new BufferedWriter(new FileWriter(tmpFile)));
			for(int i = 0; i < divPartsList.size();i++){
				if(new File(divPartsList.get(i)).lastModified() <= new File(dst + "/sha1").lastModified()){
					String sha1 = cmd.sha1sum(dst+"/"+divPartsList.get(i));
					File file = new File(divPartsList.get(i));
					String baseName =file.getName();
					bw.println(sha1 +" "+ baseName);
				}else{
					ArrayList<String> list = cmd.grep(dst+"/"+divPartsList.get(i), dst+"/sha1");
					for(int j = 0; j < list.size(); j++){
						bw.write(list.get(j));
					}
				}
			}
			bw.close();
		}catch (IOException e) {
			System.out.println(e);
		}
		tmpFile.renameTo(dstFile);
	}

	// divChangeMtimeLast
	public void divChangeMtimeLast(String s){
		String dst = followLink(s);
		String last = divLastPart(dst);
		divChangeMtime(dst,last);
	}

	//divLastPart
	public String divLastPart(String s){
		String dst = followLink(s);
		ArrayList<String> list = divParts(dst);
		return list.get(list.size()-1);
	}
	
	//divRestore
	public void divRestore(String s){
		String dst = unDivName(s);
		String src = divName(dst);
		File file = new File(dst);
		File tmpFile = divRead(src);
		
		tmpFile.renameTo(file);
		
		divRestoreTime(src);
	}
	
	// divRead
	public File divRead(String s){
		int ch = 0;
		File file = new File("tmpFile");
		ArrayList<String> list = divParts(s);
		for(int i = 0; i < list.size(); i++){
			try {
				BufferedReader br = new BufferedReader(new FileReader(new File(s+"/"+list.get(i))));
				BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
				while((ch = br.read()) != -1){
					bw.write((char)ch);
				}
				br.close();
				bw.close();
			} catch (FileNotFoundException e) {
				System.out.println(e);
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		return file;
	}
	
	//divRestoreTime
	public void divRestoreTime(String s){
		String src = followLink(s);
		String dst = unDivName(s);
		File file = new File(dst);
		File lsl = new File(src +"/ls-l");
		
		if(file.isFile() && lsl.isFile()){
			file.setLastModified(lsl.lastModified());
		}
	}
	
	public void divConcat(){
		
	}
}
