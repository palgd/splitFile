package splitFile;

import java.io.File;

public class DividedData {
	private File dividedFileDir;
	private File dividedlastFile;
	
	public DividedData(String dividedFileDirName) {
		this.dividedFileDir = new File(dividedFileDirName);
	}

	public File getDividedFileDir() {
		return dividedFileDir;
	}

	public void setDividedFileDir(File dividedFileDir) {
		this.dividedFileDir = dividedFileDir;
	}

	public File getDividedlastFile() {
		return dividedlastFile;
	}

	public void setDividedlastFile(File dividedlastFile) {
		this.dividedlastFile = dividedlastFile;
	}
}
