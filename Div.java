package splitFile;


public class Div {
	public static void main(String[] args) {
		BigDataController bigDataController = new BigDataController();
		
		for(int i = 0; i < args.length; i++){
			bigDataController.divideFile(args[i]);
		}
	}
}