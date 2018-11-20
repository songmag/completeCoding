package DataBases;
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

	private static final int LOGIN_OK = 1;
	private static final int NO_DATA = 2;
	private static final int LOGIN_FAIL = 3;

	public DataBase() {

	}

	public void LogoutClient(JsonObject data) {
		String query = "update member set flag='0' where id = '"+data.get("id")+"'";
		try {
			System.out.println("로그아웃 플래그 변경");
			stat.execute(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				closeSocket();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*
	 * Method : IsClient Argument : id (String) 고객이 맞는지 아닌지 확인을 위한 ID값 Result :
	 * Flag값
	 */
	public int IsClient(JsonObject data) {
		int returnValue = LOGIN_FAIL;
		
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

			
			// 로그인 정보를 확인하는 알고리즘
			returnValue = findLoginState(data);
			switch (returnValue) {
			case LOGIN_OK: // 로그인 성공
				String updateQuery = "update member set flag = '1' where id = '" + result.getId() + "'";// 로그인 플래그 변동
				stat.executeQuery(updateQuery);
				break;
			case LOGIN_FAIL: // 로그인 실패
				break;
			case NO_DATA: // 데이터 없음
				break;
			}

		} catch (NullPointerException e) {// 숫자가 아닌 문자열이 넘어왔을 경우
			e.printStackTrace();
		} catch (SQLException e) {//Query문에 오류가 있을 경우
			e.printStackTrace();
		} finally {
			try {
				closeSocket();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return returnValue;
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

	// 소켓을 닫기위한 메소드 (이부분을 제대로 처리하지 않으면 쓰레드가 죽어도 죽지않고 계속 메모리를 잡아먹음)
	private void closeSocket() throws Exception {
		if (rs != null)
			rs.close();
		if (conn != null)
			conn.close();
		if (stat != null)
			stat.close();
	}

	//데이터베이스에 있는 데이터를 확인하여 안드로이드로 보낼 신호를 정하는 메소드
	private int findLoginState(JsonObject data) {
		int returnValue = LOGIN_FAIL;
		
		if (result.getId() == null) {// 데이터베이스에 데이터가 없을경우
			returnValue = NO_DATA;
		} else if (result.getFlag() == 1) {// 로그인 중이라면
			System.out.println("로그인중입니다.");
			returnValue = LOGIN_FAIL;
		} else if (result.getId().equals(data.get("id").getAsString()) // 만약 아이디와 패스워드가 일치한다면
				&& result.getPassword().equals(data.get("password").getAsString())) {
			returnValue = LOGIN_OK;// 반환값을 참으로 바꿈
		}

		return returnValue;
	}
}
