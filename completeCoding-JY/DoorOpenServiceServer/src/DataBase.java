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
	private final String password = "root";

	private Connection conn; // �����ͺ��̽� ������ ���� Connection ��ü
	private Statement stat; // ������� Ȯ���ϴ� ��ü
	private ResultSet rs; // ������� �����ϴ� Set �ڷᱸ��

	private UserVO result;// �����ͺ��̽� ���̺� ���� ValueObject

	private static final int LOGIN_OK = 1;
	private static final int NO_DATA = 2;
	private static final int LOGIN_FAIL = 3;
	
	private static final int JOIN_OK = 4; //ȸ������ ����
	private static final int JOIN_FAIL = 5; // ����

	public DataBase() {

	}

	/*
	 * Method : IsClient Argument : id (String) ���� �´��� �ƴ��� Ȯ���� ���� ID�� Result :
	 * Flag��
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
			// �α��� ������ Ȯ���ϴ� �˰���
			returnValue = findLoginState(data);
			switch (returnValue) {
			case LOGIN_OK: // �α��� ����
				String updateQuery = "update member set flag = '1' where id = '" + result.getId() + "'";// �α��� �÷��� ����
				stat.executeQuery(updateQuery);
				break;
			case LOGIN_FAIL: // �α��� ����
				break;
			case NO_DATA: // ������ ����
				break;
			}

		} catch (NullPointerException e) {// ���ڰ� �ƴ� ���ڿ��� �Ѿ���� ���
			e.printStackTrace();
		} catch (SQLException e) {//Query���� ������ ���� ���
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
	
	public void logoutmember(JsonObject data) {
		String logoutquery = "update member set flag = '0' where id = '" + data.get("id") + "'";
		try {
			stat.executeQuery(logoutquery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
				try {
					closeSocket();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public int joinmember(JsonObject data) { 
		String query = "select * from member where id='" + data.get("id") +  "'";
		result = new UserVO();
		try {
			rs = stat.executeQuery(query);
			while (rs.next()) {
				result.setId(rs.getString("ID"));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(result.getId()!=null)  //id�� �̹� ���� �ҽ� ���Խ���
		{
			try {
				closeSocket();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return JOIN_FAIL;
		}
		
		//flag �� default 0 ���� ����   �������ڵ��α��ν� 1
		String joinquery = "insert into member (id,password,name,com) values ('" + data.get("id") + "','" 
				+ data.get("password") + "','" + data.get("name") + "','" + data.get("com") + "')";
		try { 
			stat.executeQuery(joinquery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				closeSocket();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return JOIN_OK;
	}
	
	public void deletemember(JsonObject data) {
		String deletequery = "delete from member where id = '" + data.get("id") + "' and password = '"  + data.get("password") + "' and flag = 1 ";
		try { 
				stat.executeQuery(deletequery);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					closeSocket();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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

	// ������ �ݱ����� �޼ҵ� (�̺κ��� ����� ó������ ������ �����尡 �׾ �����ʰ� ��� �޸𸮸� ��Ƹ���)
	private void closeSocket() throws Exception {
		if (rs != null)
			rs.close();
		if (conn != null)
			conn.close();
		if (stat != null)
			stat.close();
	}

	//�����ͺ��̽��� �ִ� �����͸� Ȯ���Ͽ� �ȵ���̵�� ���� ��ȣ�� ���ϴ� �޼ҵ�
	private int findLoginState(JsonObject data) {
		int returnValue = LOGIN_FAIL;
		
		if (result.getId() == null) {// �����ͺ��̽��� �����Ͱ� �������
			returnValue = NO_DATA;
		} else if (result.getFlag() == 1) {// �α��� ���̶��
			System.out.println("�α������Դϴ�.");
			returnValue = LOGIN_FAIL;
		} else if (result.getId().equals(data.get("id").getAsString()) // ���� ���̵�� �н����尡 ��ġ�Ѵٸ�
				&& result.getPassword().equals(data.get("password").getAsString())) {
			returnValue = LOGIN_OK;// ��ȯ���� ������ �ٲ�
		}

		return returnValue;
	}
}
