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
		String[] lsL = new String[6];
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
	public void split(String inFile, String outDir){
		int i = 1, size = 0,ret = -1;
		boolean isFlag = true;
		File od = new File(outDir);

		if(! od.exists()){
			od.mkdir();
		}

		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inFile));
			BufferedOutputStream bos = null;
			byte[] b = new byte[1026];

			while((ret = bis.read(b)) != -1){
				if(size >= 1026 || isFlag){
					File outFile = new File(outDir + "/M" + String.format("%018d", i++));
					outFile.createNewFile();
					bos = new BufferedOutputStream(new FileOutputStream(outFile));
					isFlag = false;
					size = 0;
				}
				bos.write(b,0,ret);
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
		byte[] b = new byte[1026];
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
				if(0 < sha1[i] && sha1[i] < 16){
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
}