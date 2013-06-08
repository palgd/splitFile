package splitFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) {
		div_convert(args[0]);
	}

	public static void div_convert(String src){
		String dst = div_name(src);
		File file = new File(dst);

		if(! file.isDirectory()){
			div_create(dst);
		}
	}

	public static String div_name(String fileName){
		Pattern p = Pattern.compile("\\.div$|\\.div/$");
		Matcher m = p.matcher(fileName);

		if(m.find()){
			String name = undiv_name(fileName) + ".div";
			return name;
		}else{
			return fileName +".div";
		}
	}

	public static String undiv_name(String fileName){
		String[] splitName = fileName.split("/");
		String splitLastName = splitName[splitName.length-1];
		String[] unDivName = splitLastName.split(".div");
		return unDivName[unDivName.length-1];
	}

	public static void div_create(String dst){
		File file = new File(dst);

		if(! file.isDirectory()){
			file.mkdirs();
			div_create_ls_l(div_name(dst));
		}
	}
	
	public static void div_create_ls_l(String dst){
		//touch
		File file = new File(dst);
		Calendar cal = Calendar.getInstance();

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);

		cal.clear();
		cal.set(year, month, day, hour, minute, second);
		file.setLastModified(cal.getTimeInMillis());

		// ls -l >ls-l
		File lsl = new File(dst + "/ls-l");

		if(! lsl.exists()){
			try {
				lsl.createNewFile();
			} catch (IOException e) {
				System.out.println(e);
			}
		}

		try {
			FileWriter filewriter = new FileWriter(lsl);

			if(file.canExecute()){
				filewriter.write("z");
			}else{
				filewriter.write("-");
			}

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
			// TODO çXêVì˙éû
			filewriter.close();
		} catch (IOException e) {
			System.out.println(e);;
		}
		
		file.delete();
	}
}
