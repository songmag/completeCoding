package client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Pattern;


public class ServerConnection {

    //������Ű��� ����
    private final String SERVER_IP = "121.139.102.228";//������ ������ �ּ�
    private int port = 8088;//����� ��Ʈ�ѹ�
    //������Ű��� ��ü
    private String Result = ""; //�������� ����� ����� �����ϴ� String
    private Socket socket; //����� ���� ����
    private BufferedReader reader;//������ ���Ű�ü
    private BufferedWriter writer;//������ ���۰�ü

    //����� ���� ��ü
    private UserVO user;
    private JSONObject Juser;

    //�α��� ���������� Login Activity �� �˸� callback ��ü


    private static final int LOGIN_OK = 1;
    private static final int NO_DATA = 2;
    private static final int LOGIN_FAIL = 3;
	private static final int JOIN_OK = 4; //ȸ������ ����
	private static final int JOIN_FAIL = 5; // ����

    
    
    public ServerConnection(UserVO user) {
        this.user = user;

        Juser = new JSONObject();
        //JSON ������ ����
        try {
            Juser.put("id", Integer.parseInt(user.getId()));
            Juser.put("password", Integer.parseInt(user.getPassword()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            settingSocket(); //���� ����
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (writer != null) {
            sendData();//������ ������
        }
        if (reader != null) {
            //������ �޴� ���� ������
            Thread ReceiveData = new Thread() {
                @Override
                public void run() {
                    //������ �о���̱�
                    try {
                        String line;
                        while (true) {
                            line = reader.readLine();
                            Result = line;
                            if ( (Result.equals(String.valueOf(LOGIN_OK))) || (Result.equals(String.valueOf(LOGIN_FAIL)))||(Result.equals(String.valueOf(NO_DATA))|| (Result.equals(String.valueOf(JOIN_OK))) ||(Result.equals(String.valueOf(JOIN_FAIL)))))
                                break; //true �� false �� ���ƿ��� ���� ������ ����� �Ǿ��ٴ� ���
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //������� ���� ����
                    if (Result!=null) {//������� ���ƿ��� ��
                        int flag = Integer.parseInt(Result);
                        switch (flag){
                            case LOGIN_OK :
                            	System.out.println("��������");
                                break;
                            case NO_DATA :
                            	System.out.println("��������");//��������(ID�� PASSWORD �� �ϳ��� �߸���)
                                break;
                            case LOGIN_FAIL : 
                            	System.out.println("�뵥����");//DB�� �����Ͱ� ���� ���
                                break;
                            case JOIN_OK :
                            	System.out.println("���Լ���");
                            	break;
                            case JOIN_FAIL :
                            	System.out.println("���Խ���");
                            	break;

                        }

                    }
                }
            };
            ReceiveData.start(); //������ �޾ƿ���
        }
    }


    private void sendData() {
        PrintWriter out = new PrintWriter(writer, true);
        out.println(Juser);
    }

    private void settingSocket() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(SERVER_IP, port), 10000);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
