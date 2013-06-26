package splitFile;

public class Div {
	//TODO ?
	public static void main(String[] args) {
		Livdiv livdiv = new Livdiv();
		for(int i = 0; i < args.length; i++){
			// String d = args[i] + ".div";
			livdiv.divConvert(args[i]);
		}
	}
}
