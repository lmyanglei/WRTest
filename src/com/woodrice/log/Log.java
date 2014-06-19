package com.woodrice.log;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.woodrice.file.GenFile;

public class Log {

	// file name of log file
	public static String CONST_FILE_NAME_LOG = "log";
	
	public static void main(String[] args) {
		Log log = new Log();
		log.log("it's log");
	}

	public void log(String content){
		
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		
		String logContent = "[" + dateString + "]" + " " + content;
		System.out.println(logContent);
		
		GenFile genFile = new GenFile();
		genFile.write(CONST_FILE_NAME_LOG,logContent);
	}
}
