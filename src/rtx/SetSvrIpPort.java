package rtx;

import rtx.RTXSvrApi;
public class SetSvrIpPort
 {
	//2007测试成功
    public static void main(String[] args) {

    	String SvrIP = "127.0.0.1";
    	SvrIP = "192.168.11.121";
    	int iPort = 6000;
    	
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		RtxsvrapiObj.setServerIP(SvrIP);
    		RtxsvrapiObj.setServerPort(iPort);
    		System.out.println("操作成功"+ "\n"+ "服务器地址:" +SvrIP + "\n" + "服务器端口:"+iPort);
    		
		     
	    }	
    	RtxsvrapiObj.UnInit();
    	
    }
}