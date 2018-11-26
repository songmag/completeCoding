package DataBases;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.JsonObject;

public class CompanySend extends DBConnect implements DBConnectionInterface{
	@Override
	public Object excute(JsonObject data) throws SQLException {
		ArrayList<JsonObject> return_value;
		return_value = makeList(data.get("id").toString().replace("\"",""));
		if(return_value == null)
			return LOGIN_FAIL;
		return return_value;
	}
	private ArrayList<JsonObject> makeList(String id) throws SQLException
	{
		if(!connection())
		{
			return null;
		}
		ArrayList<JsonObject> return_value;
		PreparedStatement stat;
		stat = conn.prepareStatement(COMPANYSQL);
		ResultSet res;
		res = stat.executeQuery();
		return_value = new ArrayList<JsonObject>();
		while(res.next())
		{
			CompanyVO object;
			object = new CompanyVO(res.getString("name"),res.getFloat("latitude"),res.getFloat("longtitude"),res.getFloat("ranges"));
			return_value.add(object.company);
		}
		if(return_value.size() == 0)
		{
			return_value = null;
		}
		res.close();
		stat.close();
		closeConnection();
		return return_value;
	}
}
