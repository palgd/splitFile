package splitFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;


public class DivideBigData {
	private Libdiv libdiv = new Libdiv();
	
	// divConvert file
	public void divConvert(File bigFile) {
		File outDir = libdiv.divName(bigFile);

		if(! outDir.isDirectory()){
			libdiv.divCreate(outDir);
		}
		
		divWrite(bigFile, outDir);
		libdiv.divChangeMtime(outDir, bigFile);

	}

	
	// divAppend
	public void divAppend(File f){
		int n = 0;
		File dst = libdiv.followLink(f);
		BufferedInputStream bis = new BufferedInputStream(System.in);

		if( ! dst.isDirectory()){
			libdiv.divCreate(dst);
		}

		File last = libdiv.divLastPart(dst);
		n = libdiv.divLastPartNumber(dst);

		if(last == null){
			split(bis,dst);
		} else{
			split(bis,dst, n);
		}

		libdiv.divUpdateSha1(dst);
		libdiv.divChangeSize(dst);
		libdiv.divUpdateMtime(dst);
	}
	
	// divWrite file, directory
	public void divWrite(File bigFile, File dir){
		File outDir = libdiv.followLink(dir);
		BufferedInputStream bis;
		if(bigFile ==null){
			bis = new BufferedInputStream(System.in);
		}else{
			bis = libdiv.bufferedFileInputStreamInstance(bigFile);
		}

		if(! outDir.isDirectory()){
			libdiv.divCreate(outDir);
		}else{
			divTrunc(outDir);
		}

		split(bis, outDir);
		libdiv.divUpdateSha1(outDir);
		libdiv.divChangeSize(outDir);
		libdiv.divChangeMtimeLast(outDir);
	}
	
	//divWrite directory
	public void divWrite(File outDir){
		File newOutDir = libdiv.followLink(outDir);
		BufferedInputStream bis = new BufferedInputStream(System.in);

		if(! newOutDir.isDirectory()){
			libdiv.divCreate(newOutDir);
		}else{
			divTrunc(newOutDir);
		}

		split(bis, newOutDir);

		libdiv.divUpdateSha1(newOutDir);
		libdiv.divChangeSize(newOutDir);
		libdiv.divChangeMtimeLast(newOutDir);
	}
	
	// divTrunk directory
	public void divTrunc(File outDir){
		File dst = libdiv.followLink(outDir);
		ArrayList<String> divPartsList = libdiv.divParts(dst);

		for(int i = 0; i < divPartsList.size(); i++){
			File file = new File(dst + "/" + divPartsList.get(i));

			if(file.isFile()){
				file.delete();
			}
		}

		libdiv.divChangeSize(dst);
		libdiv.divChangeMtimeNow(dst);
	}

	// split
	public void split(BufferedInputStream bigData, File outDir){
		int i = 0, size = 0,len = -1;
		boolean isFlag = true;

		if(! outDir.exists()){
			outDir.mkdir();
		}

		try {
			BufferedInputStream bis = bigData;
			BufferedOutputStream bos = null;
			byte[] b = new byte[1024];

			while((len = bis.read(b)) != -1){
				if(size >= 1024 || isFlag){
					String dividedFileName = outDir.getPath() + "/M" + String.format("%018d", i++);
					File outFile = new File(dividedFileName);

					if(! outFile.exists()){
						outFile.createNewFile();
					}
					bos = new BufferedOutputStream(new FileOutputStream(outFile));
					isFlag = false;
					size = 0;
				}
				bos.write(b,0,len);
				bos.flush();
				size++;
			}

			bis.close();
			bos.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	// split
	public void split(BufferedInputStream bigData, File outDir, int i){
		int len = -1;
		boolean isFlag = true;

		if(! outDir.exists()){
			outDir.mkdir();
		}

		try {
			BufferedInputStream bis = bigData;
			BufferedOutputStream bos = null;
			RandomAccessFile raf = null;
			byte[] b = new byte[1024];

			while((len = bis.read(b)) != -1){
				if(isFlag || raf.length() >= 1024 * 1024){
					File outFile = new File(outDir + "/M" + String.format("%018d", i++));
					raf = new RandomAccessFile(outFile, "rw");
					if(outFile.exists()){
						raf.seek(raf.length());
					}else{
						outFile.createNewFile();
					}
					bos = new BufferedOutputStream(new FileOutputStream(raf.getFD()));
					isFlag = false;
				}
				bos.write(b,0,len);
				bos.flush();
			}

			bis.close();
			bos.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
