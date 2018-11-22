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

import CapPackage.DataBase;

public class ClientThread extends Thread {
	BufferedReader reader; 
	PrintWriter writer; 
	
	Socket client;
	
	DataBase db; 
	public ClientThread(Socket client) {
		db = new DataBase();
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
		try {
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			JsonParser parser = new JsonParser();
			JsonObject data = (JsonObject) parser.parse(reader.readLine());
			int size=0;
			System.out.println("ID,PW >> : " + data.get("id") + data.get("password"));
			if(data.get("password")==null) 
				size = 1;
			else if(data.get("name")!= null)
				size = 4;
			else
				size = 2;
			int flag;
			flag = db.excute(data, size);
			writer = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
			System.out.println("Flag change : " + flag);
			writer.println(flag);
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
