package splitFile;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class Test {
	public static void main(String[] args) throws Exception{
		File file = new File(args[0]);
		String[] s = lsl(file);
		for(int i = 0; i < s.length; i++){
			System.out.print(s[i] +" ");
		}
		System.out.println();
	}
	
	public static String[] lsl(File file){
		String[] ls_l = new String[6];
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
