package CapPackage;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.google.gson.JsonObject;

public class DataBase extends ClientJob implements DBConnectionInterface{
	public final int LOGIN_OK = 1;
	public final int NO_DATA = 2;
	public final int LOGIN_FAIL = 3;
	

	@Override
	public int excute(JsonObject data,int size)
	{
		int return_value=0;
		if(connection()) {
		try {
			switch(size)
			{
				case 1:
					return_value = logout(data);
					break;
				case 2:
					return_value= login(data);
					break;
				case 4:
					return_value =  signin(data);
					break;
				default:
					return_value = 55;	
			}
		}	
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally {
		closeConnection();
		}
		}
	return return_value;
	}
	
	@Override
	protected int signin(JsonObject data) throws SQLException {
		// TODO Auto-generated method stub
		int return_value;
		System.out.println("signin start >> "+data.get("id").toString());
		
		if(checkNumber(data.get("id").toString().replace("\"", "")).equals("error")) {
			System.out.println("error");
			return LOGIN_FAIL;
		}
		PreparedStatement stat;
		String sql = "insert into member (id,password,name,univ) values (?,?,?,?)";
		stat = conn.prepareStatement(sql);
		stat.setString(1, data.get("id").toString().replace("\"",""));
		stat.setString(2, data.get("password").toString().replace("\"",""));
		stat.setString(3, data.get("name").toString().replace("\"",""));
		stat.setString(4,data.get("univ").toString().replace("\"",""));
		
		try{
			stat.executeUpdate();
		}catch(SQLException e)
		{
			System.out.println("exist name");
			return_value = LOGIN_FAIL;
		}
		stat.close();
		return_value = LOGIN_OK;
		System.out.println("signin end");
		return return_value;
	}
	@Override
	protected int login(JsonObject data) throws SQLException {
		int return_value;
		PreparedStatement stat;
		String sql = "update member set flag = 1 where id =? and password = ? ";
		stat = conn.prepareStatement(sql);
		stat.setString(1, data.get("id").toString().replace("\"",""));
		stat.setString(2, data.get("password").toString().replace("\"",""));

		if(stat.executeUpdate()==0)
		{
			return_value = LOGIN_FAIL;
		}
		else
			return_value = LOGIN_OK;
		stat.close();
		return return_value;
	}
	
	@Override
	protected int logout(JsonObject data) throws SQLException {
		int return_value;
		PreparedStatement stat;
		String sql = "update member set flag = 0 where id = ?";
		stat = conn.prepareStatement(sql);
		stat.setString(1, data.get("id").toString().replace("\"",""));
		if(stat.executeUpdate()==0)
		{
			return_value = LOGIN_FAIL;
		}
		return_value = LOGIN_OK;
		stat.close();
		return return_value;
	}
	private String checkNumber(String data)
	{
		try{
			int number=0;
			number = Integer.parseInt(data);
		}catch(Exception e)
		{
			return "error";
		}
		return data; 
	}
}