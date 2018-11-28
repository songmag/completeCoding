
import java.sql.SQLException;

import com.google.gson.JsonObject;

import DataBases.DBConnectionInterface;
import DataBases.DBFactory;
import Server.UserServer;

public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*DBFactory db;
		DBConnectionInterface i_db;
		db = new DBFactory();
		JsonObject a = new JsonObject();
		a.addProperty("id","1");
		a.addProperty("password","kteMKXxbvxlOe4Iw==");
		a.addProperty("key", 1);
		i_db = db.factory(a);
		try {
			System.out.println(i_db.excute(a));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		UserServer userServer = new UserServer();
		try {
			userServer.ServerOpen();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
