package splitFile;

public class Div {
	public static void main(String[] args) {
		Libdiv livdiv = new Libdiv();
		
		for(int i = 0; i < args.length; i++){
			livdiv.divConvert(args[i]);
		}
	}
}
