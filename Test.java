package splitFile;

import java.util.ArrayList;



public class Test {
	public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0;i<5;i++) list.add(i);
		System.out.println(list.get(list.size()-1));
	}
}
