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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Libdiv {
	// divName file
	public File divName(File file){
		Pattern p = Pattern.compile("\\.div$|\\.div/$");
		Matcher m = p.matcher(file.getPath());

		if(m.find()){
			return file;
		}else{
			return new File(file.getPath() +".div");
		}
	}

	// unDivName file.div
	public File unDivName(File file){
		Pattern p = Pattern.compile("\\.div$|\\.div/$");
		Matcher m = p.matcher(file.getPath());

		if(m.find()){
			String[] unDivName = file.getPath().split(".div");
			return new File(unDivName[unDivName.length-1]);
		}else{
			return file;
		}
	}

	// divCreate directory
	public void divCreate(File outDir){
		File linked = new File(outDir.getPath() + "/linked");
		File sha1 = new File(outDir.getPath() + "/sha1");
		File lsl = new File(outDir.getPath() + "/ls-l");

		if(! outDir.isDirectory()){
			outDir.mkdirs();
			divCreateLsL(outDir);

			linked.setLastModified(lsl.lastModified());
			sha1.setLastModified(lsl.lastModified());

			createFile(linked);
			createFile(sha1);
		}
	}

	// divCreateLsL
	public void divCreateLsL(File outDir){
		File tmpFile = new File(outDir.getPath() +"/" + outDir.getName());
		File lsl = new File(outDir.getPath() + "/ls-l");
		Calendar cal = Calendar.getInstance();

		if(tmpFile.exists()){
			tmpFile.setLastModified(cal.getTimeInMillis());
		}

		createFile(tmpFile);

		String[] l = ls_l(tmpFile);
		createFile(lsl);
		
		try {
			FileWriter fw = new FileWriter(lsl);
			for(int i = 0; i < l.length; i++){
				fw.write(l[i]+" ");
			}
			fw.close();
		} catch (IOException e) {
			System.out.println(e);
		}

		tmpFile.delete();
	}


	// followLink directory
	public File followLink(File dir){
		String tmp, newDirName = null;

		while(new File(dir+"/link").isFile()){
			try{
				BufferedReader b = new BufferedReader(new FileReader(dir));
				while(((tmp = b.readLine())!=null)){
					newDirName = tmp;
				}
				b.close();
			}catch(Exception e){
				System.out.println(e);
			}
		}

		if(newDirName != null){
			File newDir = new File(newDirName);
			return newDir;
		}

		return dir;
	}


	// divParts
	public ArrayList<String> divParts(File f){
		String src = "";
		File file = followLink(f);
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
		//divPartsList.clear();

		for(int i = 0; i < list.size(); i++){
			Matcher mref = pref.matcher(list.get(i));

			if(mref.find()){
				String d = file.getParent();

				try{
					BufferedReader b = new BufferedReader(new FileReader(d + "/refer"));
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
	public void divChangeSize(File f){
		File dst = followLink(f);
		int size = divSize(dst);
		divReplaceLsL(dst,1,size);
	}

	// divSize and divCount
	public int divSize(File f){
		int size = 0;

		ArrayList<String> divPartsList = divParts(f);

		for(int i = 0; i < divPartsList.size();i++){
			File file = new File(divPartsList.get(i));
			size = size + (int)file.length();
		}

		return size;
	}

	// divReplaceLsL
	public void divReplaceLsL(File dst, int n, int val){
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		String vmName = bean.getName();
		long pid = Long.valueOf(vmName.split("@")[0]);
		String tmp = dst+"/divReplaceLsL."+pid;
		String[] sa = divSubstrLsL(dst,n,val);
		File file = new File(tmp);
		File lsl = new File(dst+"/ls-l");

		createFile(file);

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
		lsl.delete();
		file.renameTo(lsl);

	}

	// divSubstrLsL
	public String[] divSubstrLsL(File src, int n, int val){
		String[] sa = null;
		try {
			BufferedReader b = new BufferedReader(new FileReader(src.getPath() + "/ls-l"));
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
	public void divChangeMtimeNow(File f){
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		String vmName = bean.getName();
		long pid = Long.valueOf(vmName.split("@")[0]);
		File dstFile = followLink(f);
		String now = dstFile.getPath() + "now."+ pid;
		File file = new File(now);
		Calendar cal = Calendar.getInstance();

		if(file.exists()){
			file.setLastModified(cal.getTimeInMillis());
		}

		createFile(file);

		divChangeMtime(dstFile,file);
		file.delete();
	}

	// divChangeMtime
	public void divChangeMtime(File outDir,File last){
		File dst = followLink(outDir);
		String[] lsL = ls_l(last);

		divReplaceLsL(dst, 2, Integer.valueOf(lsL[2]));
		divReplaceLsL(dst, 3, Integer.valueOf(lsL[3]));
	}

	//divUpdateSha1
	public void divUpdateSha1(File f){
		File dst = followLink(f);
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		String vmName = bean.getName();
		long pid = Long.valueOf(vmName.split("@")[0]);
		String tmp = dst.getPath() + "/div-update-sha1"+pid;
		ArrayList<String> divPartsList = divParts(dst);
		File tmpFile = new File(tmp);
		File dstFile = new File(dst + "/sha1");

		try {
			PrintWriter bw = new PrintWriter(new BufferedWriter(new FileWriter(tmpFile)));
			for(int i = 0; i < divPartsList.size();i++){
				if(new File(divPartsList.get(i)).lastModified() <= 
						new File(dst + "/sha1").lastModified()){
					String sha1 = sha1sum(dst+"/"+divPartsList.get(i));
					File file = new File(divPartsList.get(i));
					String baseName =file.getName();
					bw.println(sha1 +" "+ baseName);
				}else{
					ArrayList<String> list = grep(dst+"/"+divPartsList.get(i), dst+"/sha1");
					for(int j = 0; j < list.size(); j++){
						bw.write(list.get(j));
					}
				}
			}
			bw.close();
		}catch (IOException e) {
			System.out.println(e);
		}
		dstFile.delete();
		tmpFile.renameTo(dstFile);
	}

	// divChangeMtimeLast
	public void divChangeMtimeLast(File f){
		File dstFile = followLink(f);
		File last = divLastPart(dstFile);
		divChangeMtime(dstFile,last);
	}

	//divLastPart
	public File divLastPart(File f){
		File dst = followLink(f);
		ArrayList<String> list = divParts(dst);
		if(list.size() == 0){
			return null;
		}
		File file = new File(list.get(list.size()-1));
		return file;
	}

	// divRead
	public File divRead(File f){
		int ch = -1, fileList;
		File file = new File("tmpFile" + unDivName(f).getName());
		byte[] b = new byte[1024];
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		ArrayList<String> list = divParts(f);
		fileList = list.size();

		for(int i = 0; i < fileList; i++){
			try {
				bis = new BufferedInputStream(new FileInputStream(f+"/"+list.get(i)));
				bos = new BufferedOutputStream(new FileOutputStream(file,true));;
				while((ch = bis.read(b)) != -1){
					bos.write(b,0,ch);
				}
				bis.close();
				bos.close();
			} catch (FileNotFoundException e) {
				System.out.println(e);
			} catch (IOException e) {
				System.out.println(e);
			}
		}

		return file;
	}

	// divLastPartNumber
	public int divLastPartNumber(File dst){
		return divPartNumber(divLastPart(dst));
	}

	// divPartNumber
	public int divPartNumber(File f){
		int fileNum = 0;

		if(f == null){
			return fileNum;
		}else{
			String basename = f.getPath().split("/")[f.getPath().split("/").length - 1];
			fileNum =  Integer.valueOf( basename.replaceAll("[a-zA-Z]", ""));
			return fileNum;
		}
	}

	// divUpdateMtime
	public void divUpdateMtime(File file){
		divChangeMtimeLast(file);
	}

	// divPart
	public String divPart(int n){
		String fileName = "M" + String.format("%018d", n);
		return fileName;
	}

	// bufferedInputStream
	protected BufferedInputStream bufferedFileInputStreamInstance(File file){
		try {
			return new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		return null;
	}

	// bufferedOutputStream
	public BufferedOutputStream bufferedFileOutputStreamInstance(File file){
		try {
			return new BufferedOutputStream(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		return null;
	}

	// createFile
	private void createFile(File file){
		if(! file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

	// grep
	public ArrayList<String> grep(String pattern, String fileName){
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

	// sha1sum
	public String sha1sum(String file){
		int len=0;
		byte[] b = new byte[1024];
		String s = "";

		try{
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			MessageDigest md = MessageDigest.getInstance("SHA-1");

			while((len=bis.read(b)) != -1){
				md.update(b,0,len);
			}
			bis.close();

			byte[] sha1 = md.digest();
			for(int i = 0; i < sha1.length; i++){
				if(0 <= sha1[i] && sha1[i] < 16){
					s += "0";
				}
				s += (Integer.toHexString(sha1[i] & 0xff));
			}
		}catch(NoSuchAlgorithmException e){
			System.out.println(e);
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		return s;
	}

	// ls -l
	public String[] ls_l(File file){
		String[] l = new String[6];
		Date date = new Date(file.lastModified());
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);

		if(file.canRead()){
			l[0] = "r";
		}else{
			l[0] = "-";
		}
		
		if(file.canWrite()){
			l [0] += "w";
		}else{
			l [0] += "-";
		}
		
		if(file.canExecute()){
			l [0] += "x";
		}else{
			l [0] += "-";
		}
		
		l[1] = String.valueOf(file.length());
		l[2] = String.valueOf(month);
		l[3] = String.valueOf(day);
		l[4] = String.valueOf(hour) + ":" + String.valueOf(minute);
		l[5] = file.getName();

		return l;
	}
}
