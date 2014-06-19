package com.woodrice.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.woodrice.file.GenFile;

public class Log {

	// file name of log file
	public static String CONST_FILE_NAME_LOG = "log";
	
	public static void main(String[] args) {
		Log log = new Log();
		
		// test
		log.log("it's log");
		
		// test
		Exception e = new Exception();
		log.log(e);
	}

	/**
	 * write log content to file
	 * 
	 * @param content
	 */
	public void log(String content){
		
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		
		String logContent = "[" + dateString + "]" + " " + content;
		System.out.println(logContent);
		
		GenFile genFile = new GenFile();
		genFile.write(CONST_FILE_NAME_LOG,logContent);
	}
	
	/**
	 * write exception content to file
	 * 
	 * @param e
	 */
	public void log(Exception e){
		PrintWriter writer = null;
		try{
			File file = new File(CONST_FILE_NAME_LOG);
			if (!file.exists()) {
				file.createNewFile();
	        }
			
			writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
			e.printStackTrace(writer);
			
			writer.flush();
			writer.close();
		}catch(Exception e1){
			e1.printStackTrace();
		}finally{  
		    if (writer != null) {
				writer.close();
			}
		}
	}
}
