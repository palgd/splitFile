package splitFile;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Div_test_f {
	public static void main(String[] args) {
		try{
			File file = new File(args[0]);
			Pattern p = Pattern.compile("\\.div$|\\.div/$");
			Matcher m = p.matcher(args[0]);

			if(! file.exists()){
				System.out.println("ファイルやディレクトリが存在しません。");
			}else if(file.isFile()){
				System.out.println("ファイルでした。");
			}else if(file.isDirectory()){
				if(m.find()){
					System.out.println("分割ファイルのディレクトリです。");
				}else{
					System.out.println("分割ファイルのディレクトリではありません。");
				}
			}
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("引数が不正です。");
		}
	}
}
