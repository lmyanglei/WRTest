package com.woodrice.util;

import java.util.Map;
import java.util.Properties;

public class Util {
	
	public static void main(String[] args) {
		// test
		Util util = new Util();
		util.systemInfo();
	}

	public void systemInfo(){
		try{
			Properties properties = System.getProperties();  
			 System.out.println(properties.get("user.name"));  
			 
		    for (Map.Entry<Object, Object> entry : properties.entrySet()) {  
		    	String info = String.format("key-[%s] value-[%s]", entry.getKey(), entry.getValue());
		        System.out.println(info);  
		    }  
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
