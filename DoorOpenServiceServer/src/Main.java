
import com.google.gson.JsonObject;
import Server.UserServer;

public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UserServer userServer = new UserServer();
		try {
			userServer.ServerOpen();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
