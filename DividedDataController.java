package splitFile;



public class DividedDataController {
	CatData catData;
	CombineBigData combineBigData;
	DividedData dividedData;
	
	public DividedDataController() {
		catData = new CatData();
		combineBigData = new CombineBigData();
	}
	
	public void combinData(String dividedFileDirName) {
		dividedData = new DividedData(dividedFileDirName);
		combineBigData.divRestore(dividedData.getDividedFileDir());
	}
	
	public void cat(String dividedFileDirName) {
		dividedData = new DividedData(dividedFileDirName);
		catData.dividedFileList(dividedData.getDividedFileDir());
	}
	
	
	public DividedData getDividedData() {
		return dividedData;
	}

	public void setDividedData(DividedData dividedData) {
		this.dividedData = dividedData;
	}

	public CombineBigData getCombineBigData() {
		return combineBigData;
	}

	public void setCombineBigData(CombineBigData combineBigData) {
		this.combineBigData = combineBigData;
	}
}
