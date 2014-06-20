package com.woodrice.util;

public class StringUtil {

	/**
	 * replace multi space into one space
	 * @param str
	 * @return
	 */
	public static String replaceMultiSpace(String str){
		return str.replaceAll(" +", " ");
	}

	public static void main(String[] args) {
		
		// test
		String str = "aa    bb    c  d  e";
		System.out.println(str);
		
		str = StringUtil.replaceMultiSpace(str);
		System.out.println(str);
	}
}
