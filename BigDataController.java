package splitFile;

import java.io.File;


public class BigDataController {
	BigData bigData;
	DivideBigData divideBigData;
	
	public BigDataController() {
		divideBigData = new DivideBigData();
	}
	
	public void divideFile(String bigFileName) {
		bigData = new BigData(bigFileName);
		divideBigData.divConvert(bigData.getBigData());
	}
	
	public void divideStdOut(String outDirName) {
		File outDir = new File(outDirName);
		divideBigData.divWrite(null, outDir);
	}
	
	public void dividedAddStdOut(String outDirName){
		File outDir = new File(outDirName);
		divideBigData.divAppend(outDir);
	}

	public BigData getBigData() {
		return bigData;
	}

	public void setBigData(BigData bigData) {
		this.bigData = bigData;
	}

	public DivideBigData getDivideBigData() {
		return divideBigData;
	}

	public void setDivideBigData(DivideBigData divideBigData) {
		this.divideBigData = divideBigData;
	}
}
