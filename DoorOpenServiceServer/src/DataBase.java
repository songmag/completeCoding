import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {

	// 데이터베이스 기본 정보
	private final String address = "jdbc:mariadb://localhost:3306/dooropenservice";
	private final String user = "root";
	private final String password = "8845";

	private Connection conn; // 데이터베이스 연결을 위한 Connection 객체
	private Statement stat; // 연결상태 확인하는 객체
	private ResultSet rs; // 쿼리결과 저장하는 Set 자료구조

	private UserVO result;//데이터베이스 테이블 정보 ValueObject
	
	public DataBase() {

	}

	/*
	 * Method : IsClient Argument : id (String) 고객이 맞는지 아닌지 확인을 위한 ID값 Result :
	 * Flag값
	 */
	public boolean IsClient(String data) throws SQLException {
		
		String []userData = data.split("/");
		
		String query = "select * from member where id='" + userData[0] + "' and "+"password ='"+userData[1]+"'";
		result = new UserVO();
		try {

			rs = stat.executeQuery(query);
			while (rs.next()) {
				result.ID = rs.getString("ID");
				result.password = rs.getString("password");
				result.name = rs.getString("name");
				result.Univ = rs.getString("univ");
				result.flag = rs.getString("flag");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (conn != null)
				conn.close();
			if (stat != null)
				stat.close();

		}

		if (result.ID.equals(userData[0]) && result.password.equals(userData[1])) {
			return true;
		} else {
			return false;
		}

	}

	/*
	 * Method : connectDB Argument : null Result : MariaDB에 연결하기위한 메소드
	 */
	public void connectDB() throws SQLException {

		try {
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection(address, user, password);
			stat = conn.createStatement();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
