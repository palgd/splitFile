package splitFile;

import java.io.File;

public class CombineBigData {
	private Libdiv libdiv = new Libdiv();
	
	//divRestore
	public void divRestore(File dividedFileDir) {
		File dir = libdiv.unDivName(dividedFileDir);
		File src = libdiv.divName(dir);
		File tmpFile = libdiv.divRead(src);

		tmpFile.renameTo(new File(dir.getName()));

		divRestoreTime(src);
	}

	//divRestoreTime
	public void divRestoreTime(File f){
		File src = libdiv.followLink(f);
		File file = libdiv.unDivName(f);
		File lsl = new File(src +"/ls-l");

		if(file.isFile() && lsl.isFile()){
			file.setLastModified(lsl.lastModified());
		}
	}
}
