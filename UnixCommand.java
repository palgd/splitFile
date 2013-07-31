package splitFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnixCommand {
	/*
	// getLastModified
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

	// split
	//public void split(String inFile, String outDir, String prefix){
	public void split(BufferedInputStream bigData, String outDir, String prefix){
		int i = 0, size = 0,len = -1;
		boolean isFlag = true;
		File od = new File(outDir);

		if(! od.exists()){
			od.mkdir();
		}

		try {
			//BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inFile));
			BufferedInputStream bis = bigData;
			BufferedOutputStream bos = null;
			byte[] b = new byte[1024];

			while((len = bis.read(b)) != -1){
				if(size >= 1024 || isFlag){
					File outFile = new File(outDir + "/" + prefix + String.format("%018d", i++));
					outFile.createNewFile();
					bos = new BufferedOutputStream(new FileOutputStream(outFile));
					isFlag = false;
					size = 0;
				}
				bos.write(b,0,len);
				bos.flush();
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

	// cat
	public void cat(ArrayList<String> option, ArrayList<String> list){
		String[] splitFileName;
		String deleteFileName = "";
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		byte[] b = new byte[1024];
		int a = -1;
		try{
			for(int i = 0; i < list.size(); i++){
				File file = new File(list.get(i));
				bis = new BufferedInputStream(new FileInputStream(file));
				bos = new BufferedOutputStream(System.out);
				while((a = bis.read(b)) != -1){
					bos.write(b,0,a);
				}
				bos.close();
				splitFileName = list.get(i).split("tmpFile");
				deleteFileName = splitFileName[splitFileName.length-1];
				if(file.getName().equals("tmpFile" + deleteFileName)){
					file.delete();
				}
			}
		}catch(IOException e){
			System.out.println(e);
		}
	}
	
	//cat 
	public void cat(ArrayList<String> option){
		BufferedReader br = null;
		String s = null;

		try{
			br = new BufferedReader(new InputStreamReader(System.in));
			while((s = br.readLine()) != null){
				if(option.isEmpty()){
					System.out.print(s);
				}
			}
		}catch(IOException e){
			System.out.println(e);
		}
	}
	/*
	// cat
	public void cat(ArrayList<String> option, ArrayList<String> list){
		String[] splitFileName;
		String deleteFileName;
		BufferedReader br = null;
		int lineCount = 1;
		String s = "";

		try {
			for(int i = 0; i < list.size(); i++){
				if(list.get(i).equals("-")){
					cat(option);
				}else{
					File file = new File(list.get(i));
					br = new BufferedReader(new FileReader(file));
					
					while((s = br.readLine()) != null){
						if(option.contains("-s")){
							if(s.equals("")){
								while((s = br.readLine()) != null && s.equals("")){}
								
								System.out.println();
							}
							System.out.println(s);
						}

						if(option.isEmpty()){
							System.out.println(s);
						}

						if(option.contains("-b")){
							if(! s.equals("")) System.out.println(lineCount++ + "  " + s);
							if(s.equals("") | option.contains("-s")) System.out.println();
						}else if(option.contains("-n")){
							System.out.println(lineCount + "  " + s);
							lineCount++;
						}
					}
					splitFileName = list.get(i).split("tmpFile");
					deleteFileName = splitFileName[splitFileName.length-1];
					if(file.getName().equals("tmpFile" + deleteFileName)){
						file.delete();
					}
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}


	// cat
	public void cat(ArrayList<String> option){
		BufferedReader br = null;
		int count = 1;
		String s = "";

		try{
			br = new BufferedReader(new InputStreamReader(System.in));
			while((s = br.readLine()) != null){
				if(option.isEmpty()){
					System.out.print(s);
				}
				
				if(option.contains("-b")){
					if(! s.equals("")) System.out.println(count++ + "  " + s);
					if(s.equals("")) System.out.println();
				}else if(option.contains("-n")){
					System.out.println(count++ + "  " + s);
				}
			}
		}catch(IOException e){
			System.out.println(e);
		}
	}*/
}