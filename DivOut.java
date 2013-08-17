package splitFile;


public class DivOut {
	public static void main(String[] args) {
		BigDataController bigDataController = new BigDataController();
		
		if(args[0].equals("-a")){
			for(int i = 1; i < args.length; i++){
				bigDataController.dividedAddStdOut(args[i]);
			}
		}else{
			for(int i = 0; i < args.length; i++){
				bigDataController.divideStdOut(args[i]);
			}
		}
	}
}
