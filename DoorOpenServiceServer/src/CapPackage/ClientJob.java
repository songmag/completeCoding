package CapPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.gson.JsonObject;

abstract public class ClientJob{	
	protected final String address = "jdbc:mariadb://localhost:3306/dooropenservice";
	protected final String user = "root";
	protected final String password = "920821";

	protected Connection conn; 
	
	protected boolean connection() {
		// TODO Auto-generated method stub
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection(address, user, password);
			return true;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	protected boolean closeConnection()  {
		if (conn != null)
			try {
				conn.close();
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
	}
	abstract protected int signin(JsonObject data) throws SQLException;
	abstract protected int login(JsonObject data)throws SQLException;
	abstract protected int logout(JsonObject data)throws SQLException;
}
