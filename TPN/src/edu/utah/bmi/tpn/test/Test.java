package edu.utah.bmi.tpn.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		List<String>a= Arrays.asList("a".split(","));
		ArrayList<String>b=new ArrayList<String>();
		ArrayList<String>a=new ArrayList<String>();
		a.add("a");
		b.addAll(a);
		a.add("b");
		a.add("c");
		System.out.println(b.removeAll(a));
	}

}
