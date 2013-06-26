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
				System.out.println("�t�@�C����f�B���N�g�������݂��܂���B");
			}else if(file.isFile()){
				System.out.println("�t�@�C���ł����B");
			}else if(file.isDirectory()){
				if(m.find()){
					System.out.println("�����t�@�C���̃f�B���N�g���ł��B");
				}else{
					System.out.println("�����t�@�C���̃f�B���N�g���ł͂���܂���B");
				}
			}
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("�������s���ł��B");
		}
	}
}
