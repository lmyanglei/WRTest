package com.woodrice.log;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.woodrice.file.GenFile;

public class Log {

	public static void main(String[] args) {
		Log log = new Log();
		log.log("it's log");
	}

	public void log(String content){
		
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		
		GenFile genFile = new GenFile();
		genFile.write("log","[" + dateString + "]" + " " + content);
	}
}
