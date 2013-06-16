package splitFile;

import java.io.File;

public class Test {
	public static void main(String[] args) {
		int src =0;
		File file = new File(args[0]);
		src = (int)file.length();
		System.out.println(src);
	}
}
