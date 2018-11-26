package DataBases;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.gson.JsonObject;

public class Login extends DBConnect implements DBConnectionInterface {
	@Override
	public Object excute(JsonObject data) throws SQLException {
		if(!connection())
			return LOGIN_FAIL;		
		int return_value;
		PreparedStatement stat;
		stat = conn.prepareStatement(SIGNINSQL);
		stat.setString(1, data.get("id").toString().replace("\"",""));
		stat.setString(2, data.get("password").toString().replace("\"",""));

		if(stat.executeUpdate()==0)
		{
			return_value = LOGIN_FAIL;
		}
		else
			return_value = SUCCESS;
		stat.close();
		closeConnection();
		return return_value;
	}	
}
