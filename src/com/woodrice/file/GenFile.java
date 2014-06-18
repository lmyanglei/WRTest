package com.woodrice.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class GenFile {

	public static void main(String[] args) {
		GenFile gen = new GenFile();
		gen.write("log", "content");
	}

	/**
	 * 输出字符到文件
	 * 
	 * @param fileName 文件名
	 * @param content 字符内容
	 */
	public void write(String fileName,String content){
		BufferedWriter fw = null;
		try{
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
	        }
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8")); // 指定编码格式，以免读取时中文字符异常
			fw.append(content);
			fw.newLine();
			fw.flush();
			fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{  
		    if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
