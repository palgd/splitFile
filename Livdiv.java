package splitFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
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
	// TODO
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
	//TODO basename
	public String undiv_name(String fileName){
		String[] name = fileName.split("/");
		return name[name.length-1];
	}

	// div_create file.dir
	public void div_create(String dst){
		File file = new File(dst);

		if(! file.isDirectory()){
			file.mkdirs();
			div_create_ls_l(div_name(dst));
		}
	}

	public void div_create_ls_l(String dst){
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
		File lsl = new File("ls-l");

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
				filewriter.write("");
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
			filewriter.close();
		} catch (IOException e) {
			System.out.println(e);;
		}
	}

	// div_write file.dir
	public void div_write(String s){
		String dst = follow_link(s);
		File file = new File(dst);

		if(! file.isDirectory()){
			div_create(dst);
		}
	}

	public String follow_link(String s){
		String src = s + "/link";
		File file = new File(src);

		while(file.isFile()){

		}

		return src;
	}


}
