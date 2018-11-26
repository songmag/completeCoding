package DataBases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect{	
	protected final String address = "jdbc:mariadb://localhost:3306/dooropenservice";
	protected final String user = "root";
	protected final String password = "920821";
	public final int LOGIN_OK = 1;
	public final int NO_DATA = 2;
	public final int LOGIN_FAIL = 3;
	public final int DUPLICATE_ID = 4;
	public final int SUCCESS = 5;
	protected final String SIGNUPSQL = "insert into member (id,password,name) values (?,?,?)";
	protected final String SIGNINSQL = "update member set flag = 1 where id =? and password = ?";
	protected final String LOGOUTSQL = "update member set flag = 0 where id = ?";
	protected final String COMPANYSQL = "Select * from company where company.name in (Select name from connect_company where id = ?";
	protected final String CONNECTSQL = "insert into connect_company values(?,?)";
	
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
}
