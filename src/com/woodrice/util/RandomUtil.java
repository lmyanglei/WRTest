package com.woodrice.util;

import java.util.Random;

public class RandomUtil {
	
	/**
	 * 返回一个随机数
	 * 
	 * @param i 数字的位数
	 * @return
	 */
	public static String getRandom(int i) {
		Random jjj = new Random();
		if (i == 0)
			return "";
		String jj = "";
		for (int k = 0; k < i; k++) {
			jj = jj + jjj.nextInt(9);
		}
		return jj;
	}
	
	// test
	public static void main(String[] args) {
		System.out.println(RandomUtil.getRandom(6));
	}
}
