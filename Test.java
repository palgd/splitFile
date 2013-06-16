package splitFile;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class Test {
	public static void main(String[] args) {
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		String vmName = bean.getName();
		long pid = Long.valueOf(vmName.split("@")[0]);
		String a = "d" + pid;
		System.out.println(a);
	}
}
