package splitFile;

import java.io.File;

public class BigData {
	private File bigData;

	public BigData(String bigFileName) {
		bigData = new File(bigFileName);
	}
	
	public File getBigData() {
		return bigData;
	}

	public void setBigData(File monoBigFile) {
		this.bigData = monoBigFile;
	}
}