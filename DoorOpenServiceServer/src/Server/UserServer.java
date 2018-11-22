package Server;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;



public class UserServer {
	ArrayList<Socket> ThreadList ;
	public static final int ServerPort = 5050;// 사용할 서버포트

	ServerSocket serverSocket = null; // 서버 소켓 (연결되는 클라이언트 소켓을 검사하기 위함)
//	Socket client;// 클라이언트 소켓 (연결된 클라이언트 소켓을 결정하기 위함

	InetAddress local;// 현재 사용하고 있는 ServerPC의 IP값을 가져오기 위한 객체 (Debug용)


	public UserServer() {
		ThreadList = new ArrayList<Socket>();
		/*
		 * TODO Client리스트를 만들어서 초기화
		 * 
		 */
	}

	public void ServerOpen() throws Exception {
		try {
			local = InetAddress.getLocalHost();
			String ip = local.getHostAddress();
			System.out.println("IP : " + ip);

			serverSocket = new ServerSocket(ServerPort);
			System.out.println("Server Open");
			
			while (true) {
				Socket client = serverSocket.accept();
				
				System.out.println("Client 연결");
				//TODO Client 리스트에서 올바른 client객체를 넘겨주기
				ClientThread clientThread = new ClientThread(client);
				clientThread.start();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null) {
				serverSocket.close();
			}
		}

	}
}
