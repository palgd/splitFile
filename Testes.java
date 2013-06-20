package splitFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class Testes {
	public static void main(String[] args) {
		split(args[0],args[1]);
	}

	public static void split(String inFile, String outDir){
		int i = 1, size = 0, ret = -1;
		boolean isFlag = true;
		File od = new File(outDir);

		if(! od.exists()){
			od.mkdir();
		}

		try {
			BufferedInputStream buf = new BufferedInputStream(new FileInputStream(inFile));
			BufferedOutputStream os = null;
			
			byte[] b = new byte[1024];

			while((ret = buf.read(b)) != -1){
				if(size >= 100 || isFlag){
					File outFile = new File(outDir + "/M" + String.format("%018d", i++));
					outFile.createNewFile();
					os = new BufferedOutputStream(new FileOutputStream(outFile));
					size = 0;
					isFlag = false;
				}
				os.write(b,0, ret);
				size++;
			}

			buf.close();
			os.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

	}
}
