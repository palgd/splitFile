package splitFile;


public class DivCat {
	public static void main(String[] args) {
		DividedDataController dividedDataController = new DividedDataController();
		
		for(int i = 0; i < args.length; i++){
			dividedDataController.cat(args[i]);
		}
	}
}
