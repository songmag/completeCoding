import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonObject;

public class DataBase {

	// �����ͺ��̽� �⺻ ����
	private final String address = "jdbc:mariadb://localhost:3306/dooropenservice";
	private final String user = "root";
	private final String password = "8845";

	private Connection conn; // �����ͺ��̽� ������ ���� Connection ��ü
	private Statement stat; // ������� Ȯ���ϴ� ��ü
	private ResultSet rs; // ������� �����ϴ� Set �ڷᱸ��

	private UserVO result;// �����ͺ��̽� ���̺� ���� ValueObject

	public DataBase() {

	}

	/*
	 * Method : IsClient Argument : id (String) ���� �´��� �ƴ��� Ȯ���� ���� ID�� Result :
	 * Flag��
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
			
			if(result.getFlag()==1) {//�α��� ���̶��
				System.out.println("�α������Դϴ�.");
				return false;
			}
			if (result.getId().equals(data.get("id").getAsString()) //���� ���̵�� �н����尡 ��ġ�Ѵٸ�
					&& result.getPassword().equals(data.get("password").getAsString())) {
				String updateQuery = "update member set flag = '1' where id = '" + result.getId() + "'";// �α��� �÷��� ����
				stat.executeQuery(updateQuery);
				returnValue = true;//��ȯ���� ������ �ٲ�
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

	//������ �ݱ����� �޼ҵ� (�̺κ��� ����� ó������ ������ �����尡 �׾ �����ʰ� ��� �޸𸮸� ��Ƹ���)
	void closeSocket() throws Exception {
		if (rs != null)
			rs.close();
		if (conn != null)
			conn.close();
		if (stat != null)
			stat.close();
	}

	/*
	 * Method : connectDB Argument : null Result : MariaDB�� �����ϱ����� �޼ҵ�
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
