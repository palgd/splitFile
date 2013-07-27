package splitFile;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DivTouch {
	// TODO 
	public static void main(String[] args) {
		Libdiv livdiv = new Libdiv();
		Pattern p = Pattern.compile("\\.div$|\\.div/$");
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		String vmName = bean.getName();
		long pid = Long.valueOf(vmName.split("@")[0]);
		
		for(int i = 0; i < args.length; i++){
			Matcher m = p.matcher(args[i]);
			if(m.find() && ! (new File(args[i]).exists())){
				livdiv.divCreate(args[i]);
			}else if(m.find()){
				String tmp = args[i] + pid;
				File file = new File(tmp);
				livdiv.divChangeMtime(args[i], tmp);
				if(file.isFile()){
					file.delete();
				}
			}
		}
	}
}
