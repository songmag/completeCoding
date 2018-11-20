import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UserServer {
	public static final int ServerPort = 8088;//����� ������Ʈ
	
	ServerSocket serverSocket = null; //���� ���� (����Ǵ� Ŭ���̾�Ʈ ������ �˻��ϱ� ����)
	Socket client;//Ŭ���̾�Ʈ ���� (����� Ŭ���̾�Ʈ ������ �����ϱ� ����)
	
	InetAddress local;//���� ����ϰ� �ִ� ServerPC�� IP���� �������� ���� ��ü (Debug��)
	
	BufferedReader reader; //Client���� ���� �����͸� �б� ���� ��ü
	PrintWriter writer; //Client�� �����͸� ������ ���� ��ü

	DataBase db; //DataBase���� ��ü
	
	
	public UserServer() {
		try {
			db = new DataBase();
			ServerOpen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void ServerOpen() throws Exception {
		
		local = InetAddress.getLocalHost();
		String ip = local.getHostAddress();
		System.out.println("IP : " + ip);

		serverSocket = new ServerSocket(ServerPort);
		System.out.println("Server Open");
		try {
			
			while (true) {
				// ���� ���� ��û�� �ö����� ���
				client = serverSocket.accept();
				System.out.println("Client ����");
				reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				JsonParser parser = new JsonParser();
				JsonObject data = (JsonObject)parser.parse(reader.readLine());
				System.out.println("�о���� �� : " + data.get("id") + data.get("password") + data.get("name") + data.get("com") + data.get("flag"));
				db.connectDB();// db�� ����
				
				memberjoin(data);
				//serverorder(data); //��� ���°ſ����� ����
				
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
	
	
	void login(JsonObject data) throws IOException //�α���
	{
		int flag = db.IsClient(data);// �ִ� ������ �ƴ��� Ȯ��
		writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),
				true);
		System.out.println("���� �� : " + flag);
		writer.println(flag);
	}
	
	void logout(JsonObject data)
	{
		db.logoutmember(data);
	}
	
	void memberjoin(JsonObject data) throws IOException  //ȸ������ 
	{
		int flag = db.joinmember(data);
		writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),
				true);
		System.out.println("���� �� : " + flag);
		writer.println(flag);
	}
	
	void memberdelete(JsonObject data) //ȸ������
	{
		db.deletemember(data);
	}
	
	void serverorder(JsonObject data) throws IOException
	{
		// ���̵� ��й�ȣ �÷���0 ������ �α��� , ���̵�  �÷���1 �α׾ƿ�,  4���� ������+�÷��װ�0�̸� ȸ������,  ���̵� ��й�ȣ �÷���1�̸� ȸ��Ż��
		if (data.get("flag").getAsInt()==0)
		{
			if(data.get("id")!=null&&data.get("password")!=null)
			{
				login(data);
			}
			else if(data.get("id")!=null&&data.get("password")!=null&&data.get("name")!=null&&data.get("company")!=null)
			{
				memberjoin(data);
			}
		}
		else if(data.get("flag").getAsInt()==1)
		{
			if(data.get("id")!=null)
			{
				logout(data);
			}
			else if(data.get("id")!=null&&data.get("password")!=null)
			{
				memberdelete(data);
			}
		}
		
	}
}
