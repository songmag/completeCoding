package Server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import DataBases.DBConnectionInterface;
import DataBases.DataBaseFactory;

public class ClientThread extends Thread {
	BufferedReader reader; 
	PrintWriter writer; 
	Socket client;
	
	DataBaseFactory db; 
	public ClientThread(Socket client) {
		this.client = client;
	}
	private void closeSocket() throws IOException{
		if (reader != null)
			reader.close();
		if (writer != null)
			writer.close();
		if (client != null)
			client.close();
	}
	@Override
	public void run() {
		DBConnectionInterface i_db;
		db = new DataBaseFactory();
		try {
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			JsonParser parser = new JsonParser();
			JsonObject data = (JsonObject) parser.parse(reader.readLine());
			System.out.println("ID,PW >> : " + data.get("id") + data.get("password"));
			i_db = db.factory(data);
			writer = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
			System.out.println("Flag change : " + i_db.excute(data));
			writer.println(i_db.excute(data));
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				closeSocket();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
