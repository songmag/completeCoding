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
	public static final int ServerPort = 8088;//사용할 서버포트
	
	ServerSocket serverSocket = null; //서버 소켓 (연결되는 클라이언트 소켓을 검사하기 위함)
	Socket client;//클라이언트 소켓 (연결된 클라이언트 소켓을 결정하기 위함)
	
	InetAddress local;//현재 사용하고 있는 ServerPC의 IP값을 가져오기 위한 객체 (Debug용)
	
	BufferedReader reader; //Client에서 보낸 데이터를 읽기 위한 객체
	PrintWriter writer; //Client로 데이터를 보내기 위한 객체

	DataBase db; //DataBase관리 객체
	
	
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
				// 소켓 접속 요청이 올때까지 대기
				client = serverSocket.accept();
				System.out.println("Client 연결");
				reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				JsonParser parser = new JsonParser();
				JsonObject data = (JsonObject)parser.parse(reader.readLine());
				System.out.println("읽어들인 값 : " + data.get("id") + data.get("password") + data.get("name") + data.get("com") + data.get("flag"));
				db.connectDB();// db와 연결
				
				serverorder(data); //명령 들어온거에따라 실행
				
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
	
	
	void login(JsonObject data) throws IOException //로그인
	{
		int flag = db.IsClient(data);// 있는 고객인지 아닌지 확인
		writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),
				true);
		System.out.println("보낼 값 : " + flag);
		writer.println(flag);
	}
	
	void logout(JsonObject data)
	{
		db.logoutmember(data);
	}
	
	void memberjoin(JsonObject data) throws IOException  //회원가입 
	{
		int flag = db.joinmember(data);
		writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),
				true);
		System.out.println("보낼 값 : " + flag);
		writer.println(flag);
	}
	
	void memberdelete(JsonObject data) //회원삭제
	{
		db.deletemember(data);
	}
	
	void serverorder(JsonObject data) throws IOException
	{
		// 아이디 비밀번호 플래그0 들어오면 로그인 , 아이디  플래그1 로그아웃,  4가지 들어오면+플래그가0이면 회원가입,  아이디 비밀번호 플래그1이면 회원탈퇴
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
