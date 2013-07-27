package splitFile;

public class DivOut {
	public static void main(String[] args) {
		Libdiv libdiv = new Libdiv();
		if(args[0].equals("-a")){
			for(int i = 1; i < args.length; i++){
				libdiv.divAppend(args[i]);
			}
		}else{
			for(int i = 0; i < args.length; i++){
				libdiv.divWrite(args[i]);
			}
		}
	}
}
