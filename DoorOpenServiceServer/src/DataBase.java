import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {

	// �����ͺ��̽� �⺻ ����
	private final String address = "jdbc:mariadb://localhost:3306/dooropenservice";
	private final String user = "root";
	private final String password = "8845";

	private Connection conn; // �����ͺ��̽� ������ ���� Connection ��ü
	private Statement stat; // ������� Ȯ���ϴ� ��ü
	private ResultSet rs; // ������� �����ϴ� Set �ڷᱸ��

	private UserVO result;//�����ͺ��̽� ���̺� ���� ValueObject
	
	public DataBase() {

	}

	/*
	 * Method : IsClient Argument : id (String) ���� �´��� �ƴ��� Ȯ���� ���� ID�� Result :
	 * Flag��
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
	 * Method : connectDB Argument : null Result : MariaDB�� �����ϱ����� �޼ҵ�
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
