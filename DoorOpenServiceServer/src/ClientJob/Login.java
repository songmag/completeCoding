package ClientJob;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.JsonObject;

import DataBases.DBConnect;
import DataBases.DBConnectionInterface;

public class Login extends DBConnect implements DBConnectionInterface {
	public Login()
	{
		super();
	}
	
	@Override
	public Object excute(JsonObject data) throws SQLException {
		if(!connection())
			return LOGIN_FAIL;		
		int return_value=0;
		if(!login(data))
		{
			return_value = checkerror(data);
		}
		else
			return_value = LOGIN_OK;
		closeConnection();
		return return_value;
	}
	private int checkerror(JsonObject data) throws SQLException{
		PreparedStatement stat;
		ResultSet res;
		int return_value=0;
		stat = conn.prepareStatement(ERRORCHECKSQL);
		stat.setString(1, data.get("id").toString().replace("\"",""));
		res = stat.executeQuery();
		if(res.next())
		{
			if(res.getInt("flag")== 1)
			{
				System.out.println("login already");
				return_value = LOGIN_FAIL;
			}
			else
			{
				System.out.println("password error");
				return_value = NO_DATA;
			}
		}
		else
		{
			return_value = NO_DATA;
		}
		stat.close();
		res.close();
		return return_value;
	}
	
	private boolean login(JsonObject data) throws SQLException{
		boolean return_value;
		PreparedStatement stat;
		stat = conn.prepareStatement(SIGNINSQL);
		stat.setString(1, data.get("id").toString().replace("\"",""));
		stat.setString(2, data.get("password").toString().replace("\"",""));
		if(stat.executeUpdate()==0)
		{
			return_value = false;
		}
		else
			return_value = true;
		stat.close();
		return return_value;
	}
}
