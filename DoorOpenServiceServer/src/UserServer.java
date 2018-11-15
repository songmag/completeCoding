import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class UserServer {
	public static final int ServerPort = 5050;// ����� ������Ʈ

	ServerSocket serverSocket = null; // ���� ���� (����Ǵ� Ŭ���̾�Ʈ ������ �˻��ϱ� ����)
	Socket client;// Ŭ���̾�Ʈ ���� (����� Ŭ���̾�Ʈ ������ �����ϱ� ����)

	InetAddress local;// ���� ����ϰ� �ִ� ServerPC�� IP���� �������� ���� ��ü (Debug��)

	BufferedReader reader; // Client���� ���� �����͸� �б� ���� ��ü
	PrintWriter writer; // Client�� �����͸� ������ ���� ��ü

	DataBase db; // DataBase���� ��ü

	public UserServer() {
		db = new DataBase();
	}

	public void ServerOpen() throws Exception {
		try {
			local = InetAddress.getLocalHost();
			String ip = local.getHostAddress();
			System.out.println("IP : " + ip);

			serverSocket = new ServerSocket(ServerPort);
			System.out.println("Server Open");
			while (true) {
				client = serverSocket.accept();
				System.out.println("Client ����");

				try {
					reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
					
					JsonParser parser = new JsonParser();
					JsonObject data = (JsonObject)parser.parse(reader.readLine());
					
					System.out.println("�о���� �� : " + data.get("id") + data.get("password"));

					db.connectDB();// db�� ����
					int flag = db.IsClient(data);// �ִ� ������ �ƴ��� Ȯ��

					writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),
							true);
					System.out.println("���� �� : " + flag);
					writer.println(flag);

				} catch (Exception e) {
					e.printStackTrace();
				}

				finally {
					if (reader != null)
						reader.close();
					if (writer != null)
						writer.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null) {
				client.close();

			}
			if (serverSocket != null) {
				serverSocket.close();
			}
		}
	}
}
