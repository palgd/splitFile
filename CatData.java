package splitFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CatData {
	Libdiv libdiv = new Libdiv();

	public void dividedFileList(File dividedFileDir) {
		String dividedFileDirName = dividedFileDir.getPath();
		ArrayList<String> arg = new ArrayList<String>();
		ArrayList<String> option = new ArrayList<String>();
		int ch = -1;
		Pattern p = Pattern.compile("^-.$");
		Matcher m = p.matcher(dividedFileDirName);

		try {
			if(dividedFileDirName.equals("--help") || dividedFileDirName.equals("--version")){
				BufferedReader br = new BufferedReader(new FileReader(dividedFileDir));
				while((ch = br.read()) != -1){
					System.out.println((char)ch);
				}
				br.close();
				return;
			}else if(dividedFileDirName.equals("-")){
				arg.add(dividedFileDirName);
			}else if(m.find()){
				option.add(dividedFileDirName);
			}else{
				arg.add(dividedFileDirName);
			}
		}catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

		if(arg.isEmpty()){
			cat(option);
		}else{
			cat(option, divConcat(arg));
		}
	}

	// divConcat
	public ArrayList<String> divConcat(ArrayList<String> arg){
		ArrayList<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile("\\.div$|\\.div/$");

		for(int i = 0; i < arg.size(); i++){
			Matcher m = p.matcher(arg.get(i));

			if(m.find()){
				File file = libdiv.divRead(new File(arg.get(i)));
				list.add(file.getPath());
			}else{
				list.add(arg.get(i));
			}
		}

		return list;
	}

	// cat
	public void cat(ArrayList<String> option, ArrayList<String> list){
		String[] splitFileName;
		String deleteFileName = "", s;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		byte[] b = new byte[1024];
		int n = -1;

		try{
			for(int i = 0; i < list.size(); i++){

				if(list.get(i).equals("-")){
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					while((s = br.readLine()) != null){
						if(option.isEmpty()){
							System.out.println(s);
						}
					}
				}else{
					File file = new File(list.get(i));
					bis = new BufferedInputStream(new FileInputStream(file));
					bos = new BufferedOutputStream(System.out);
					while((n = bis.read(b)) != -1){
						bos.write(b,0,n);
					}
					bos.flush();

					splitFileName = list.get(i).split("tmpFile");
					deleteFileName = splitFileName[splitFileName.length-1];
					if(file.getName().equals("tmpFile" + deleteFileName)){
						file.delete();
					}
				}
			}

			bis.close();
			bos.close();
		}catch(IOException e){
			System.out.println(e);
		}
	}

	//cat 
	public void cat(ArrayList<String> option){
		BufferedReader br = null;
		String s = null;

		try{
			br = new BufferedReader(new InputStreamReader(System.in));
			while((s = br.readLine()) != null){
				if(option.isEmpty()){
					System.out.print(s);
				}
			}
		}catch(IOException e){
			System.out.println(e);
		}
	}
}
