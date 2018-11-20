import java.net.ServerSocket;

public class Server implements Runnable {
	public static final int ServerPort = 3306;
	public static final String ServerIP = "218.147.100.85";
	@Override
	public void run() {
		
		try {
			ServerSocket socket = new ServerSocket(ServerPort);
			while(true) {
				
			}
		}catch(Exception e) {
			
		}
		
	}

}
