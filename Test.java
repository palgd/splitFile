package splitFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;


public class Test {
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
		File fa = new File(args[1]);
		FileWriter f = new FileWriter(file);
		long lastModifytime = fa.lastModified();
		Date date = new Date(lastModifytime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.MONTH;
		f.write("aa "+ file.length() +" " + ((cal.MONTH) +1) +" " + (cal.DATE) +" "+ file.getName());
		f.close();
	}
}
