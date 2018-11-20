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
	BufferedReader reader; // Client���� ���� �����͸� �б� ���� ��ü
	PrintWriter writer; // Client�� �����͸� ������ ���� ��ü
	
	Socket client;
	
	DataBase db; // DataBase���� ��ü
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
			
			System.out.println("�о���� �� : " + data.get("id") + data.get("password"));

			db.connectDB();// db�� ����
			
			if(data.get("password")==null) {//�α׾ƿ� 
				System.out.println("�α׾ƿ�");
				db.LogoutClient(data);
			}
			else {//�α���
				int flag = db.IsClient(data);// �ִ� ������ �ƴ��� Ȯ��

				writer = new PrintWriter(
						new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
				System.out.println("���� �� : " + flag);
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
