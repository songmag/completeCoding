package DataBases;


import com.google.gson.JsonObject;

public class DataBaseFactory {

	public DBConnectionInterface factory(JsonObject data)
	{
		if(data.get("id") != null && data.get("password") != null && data.get("company") != null && data.get("name")!= null)
			return new SignUp();
		else if(data.get("id") != null && data.get("password") != null)
			return new Login();
		else if(data.get("id") != null && data.get("company") != null)
			return new CompanySend();
		return new Logout();
	}

}
