package com.woodrice.util;

import java.io.UnsupportedEncodingException;

public class CharSetUtils {

	public static void main(String[] args) {
		// test
		String str = "字符";
		try {
			System.out.println(str);
			
			str = CharSetUtils.changeCharSet(str, "UTF-8", "GB2312");
			System.out.println(str);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * change the str char set
	 * @param str
	 * @param srcCharSet
	 * @param dstCharSet
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String changeCharSet(String str,String srcCharSet,String dstCharSet) throws UnsupportedEncodingException{
		return str = new String(str.toString().getBytes(srcCharSet),dstCharSet);
	}
	
}
