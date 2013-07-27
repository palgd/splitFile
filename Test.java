package splitFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


public class Test {
	public static void main(String[] args) {
		RandomAccessFile raf = null;
		long  n = 1024, m;
		File file = new File(args[0]);
		byte[] b = new byte[1024];

		try {
			raf = new RandomAccessFile(file,"rw");
			m = raf.length();
			while(raf.read(b) != -1 | n <= m){
				raf.seek(raf.length());
				raf.write(b);
				raf.seek(n);
				n = n + 1024;
			}
			raf.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}