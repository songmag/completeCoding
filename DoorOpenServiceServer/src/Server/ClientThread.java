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

import DataBases.DataBase;

public class ClientThread extends Thread {
	BufferedReader reader; // Client에서 보낸 데이터를 읽기 위한 객체
	PrintWriter writer; // Client로 데이터를 보내기 위한 객체
	
	Socket client;
	
	DataBase db; // DataBase관리 객체
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
			
			System.out.println("읽어들인 값 : " + data.get("id") + data.get("password"));

			db.connectDB();// db와 연결
			
			if(data.get("password")==null) {//로그아웃 
				System.out.println("로그아웃");
				db.LogoutClient(data);
			}
			else {//로그인
				int flag = db.IsClient(data);// 있는 고객인지 아닌지 확인

				writer = new PrintWriter(
						new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
				System.out.println("보낼 값 : " + flag);
				writer.println(flag);
			}
			

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
