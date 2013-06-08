package splitFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Test {
	public static void main(String[] args) { 
		File file = new File(args[0]);
		File lsl = new File(args[1]);
		
		if(! lsl.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println(e);;
			}
		}
		
		try {
			FileWriter filewriter = new FileWriter(file);
			
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
}
