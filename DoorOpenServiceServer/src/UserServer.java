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
	public static final int ServerPort = 5050;// 사용할 서버포트

	ServerSocket serverSocket = null; // 서버 소켓 (연결되는 클라이언트 소켓을 검사하기 위함)
	Socket client;// 클라이언트 소켓 (연결된 클라이언트 소켓을 결정하기 위함)

	InetAddress local;// 현재 사용하고 있는 ServerPC의 IP값을 가져오기 위한 객체 (Debug용)

	BufferedReader reader; // Client에서 보낸 데이터를 읽기 위한 객체
	PrintWriter writer; // Client로 데이터를 보내기 위한 객체

	DataBase db; // DataBase관리 객체

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
				System.out.println("Client 연결");

				try {
					reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
					
					JsonParser parser = new JsonParser();
					JsonObject data = (JsonObject)parser.parse(reader.readLine());
					
					System.out.println("읽어들인 값 : " + data.get("id") + data.get("password"));

					db.connectDB();// db와 연결
					int flag = db.IsClient(data);// 있는 고객인지 아닌지 확인

					writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),
							true);
					System.out.println("보낼 값 : " + flag);
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
