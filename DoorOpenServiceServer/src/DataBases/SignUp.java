package DataBases;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.gson.JsonObject;

public class SignUp  extends DBConnect implements DBConnectionInterface{

	@Override
	public Object excute(JsonObject data) throws SQLException {
		// TODO Auto-generated method stub
		if(!connection())
			return LOGIN_FAIL;
		int return_value = 0;
				System.out.println("signin start >> "+data.get("id").toString());
				PreparedStatement stat;
				stat = conn.prepareStatement(SIGNUPSQL);
				stat.setString(1, data.get("id").toString().replace("\"",""));
				stat.setString(2, data.get("password").toString().replace("\"",""));
				stat.setString(3, data.get("name").toString().replace("\"",""));
				try{
						stat.executeUpdate();
					}catch(SQLException e)
					{
						System.out.println("exist name");
						return_value = DUPLICATE_ID;
					}
				stat = conn.prepareStatement(CONNECTSQL);
				stat.setString(1, data.get("id").toString().replace("\"", ""));
				stat.setString(2, data.get("company").toString().replace("\"", ""));
				try {
					stat.executeUpdate();
				}catch(SQLException e)
				{
					System.out.println("company no exist");
					return_value = DUPLICATE_ID;
				}
				stat.close();
				if(return_value == 0)
					return_value = LOGIN_OK;
				System.out.println("signin end");
				closeConnection();
				return return_value;
	}

}