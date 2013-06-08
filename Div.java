package splitFile;

public class Div {
	public static void main(String[] args) {
		Livdiv livdiv = new Livdiv();
		int length = args.length - 1;
		
		for(int i = 0; i < length;i++){
			String d = args[i] + ".div";
			livdiv.div_convert(args[i]);
			
		}
	}
}