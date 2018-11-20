package client;


public class Main {
	private static UserVO user;
	 
	public static void main(String[] args) 
	{
		user = new UserVO("112233","1111",0,null,null);
		ServerConnection ServerConnection = new ServerConnection(user);
		
	}

	
}
