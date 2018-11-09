import java.net.ServerSocket;

public class Server implements Runnable {
	public static final int ServerPort = 3306;
	public static final String ServerIP = "9.4.0.5125";
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
