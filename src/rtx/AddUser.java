package rtx;

import rtx.RTXSvrApi;
public class AddUser
 {
	//2007测试成功
    public static void main(String[] args) {

    	String userName = "testuser3";
    	String deptID = "1";
    	String chsName = "测试用户3";
    	String pwd = "123";
    	
    	int iRet = -1;
    	
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		iRet =RtxsvrapiObj.addUser(userName,deptID,chsName,pwd);
    		if (iRet==0)
    		{
    			System.out.println("添加成功");
    		}
    		else 
    		{
    			System.out.println("添加失败");
    		}

	    }	
    	RtxsvrapiObj.UnInit();
    	
    }
}
