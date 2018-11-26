package DataBases;

import com.google.gson.JsonObject;

public class CompanyVO{
	JsonObject company;
	
	public CompanyVO(String name,float latitude,float longtitude,float range)
	{
		company = new JsonObject();
		company.addProperty("name", name);
		company.addProperty("latitude",Float.toString(latitude));
		company.addProperty("longtitude",Float.toString(longtitude));
		company.addProperty("range", Float.toString(range));
	}
}