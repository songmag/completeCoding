package DataBases;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.gson.JsonObject;

public class Logout extends DBConnect implements DBConnectionInterface{

	@Override
	public Object excute(JsonObject data) throws SQLException {
		if(!connection())
			return LOGIN_FAIL;
		int return_value;
		PreparedStatement stat;
		stat = conn.prepareStatement(LOGOUTSQL);
		stat.setString(1, data.get("id").toString().replace("\"",""));
		if(stat.executeUpdate()==0)
		{
			return_value = LOGIN_FAIL;
		}
		return_value = LOGIN_OK;
		stat.close();
		closeConnection();
		return return_value;
	}
}
