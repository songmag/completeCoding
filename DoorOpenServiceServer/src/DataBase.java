import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonObject;

public class DataBase {

	// 데이터베이스 기본 정보
	private final String address = "jdbc:mariadb://localhost:3306/dooropenservice";
	private final String user = "root";
	private final String password = "8845";

	private Connection conn; // 데이터베이스 연결을 위한 Connection 객체
	private Statement stat; // 연결상태 확인하는 객체
	private ResultSet rs; // 쿼리결과 저장하는 Set 자료구조

	private UserVO result;// 데이터베이스 테이블 정보 ValueObject

	public DataBase() {

	}

	/*
	 * Method : IsClient Argument : id (String) 고객이 맞는지 아닌지 확인을 위한 ID값 Result :
	 * Flag값
	 */
	public boolean IsClient(JsonObject data){
		boolean returnValue = false;
		String query = "select * from member where id='" + data.get("id") + "' and " + "password ='"
				+ data.get("password") + "'";
		result = new UserVO();
		try {

			rs = stat.executeQuery(query);
			while (rs.next()) {
				result.setId(rs.getString("ID"));
				result.setPassword(rs.getString("password"));
				result.setFlag(rs.getInt("flag"));
			}
			
			if(result.getFlag()==1) {//로그인 중이라면
				System.out.println("로그인중입니다.");
				return false;
			}
			if (result.getId().equals(data.get("id").getAsString()) //만약 아이디와 패스워드가 일치한다면
					&& result.getPassword().equals(data.get("password").getAsString())) {
				String updateQuery = "update member set flag = '1' where id = '" + result.getId() + "'";// 로그인 플래그 변동
				stat.executeQuery(updateQuery);
				returnValue = true;//반환값을 참으로 바꿈
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				closeSocket();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return returnValue;
	}

	//소켓을 닫기위한 메소드 (이부분을 제대로 처리하지 않으면 쓰레드가 죽어도 죽지않고 계속 메모리를 잡아먹음)
	void closeSocket() throws Exception {
		if (rs != null)
			rs.close();
		if (conn != null)
			conn.close();
		if (stat != null)
			stat.close();
	}

	/*
	 * Method : connectDB Argument : null Result : MariaDB에 연결하기위한 메소드
	 */
	public void connectDB() {

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
