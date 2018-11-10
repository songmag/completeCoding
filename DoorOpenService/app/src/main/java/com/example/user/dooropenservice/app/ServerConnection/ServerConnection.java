package com.example.user.dooropenservice.app.ServerConnection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;


public class ServerConnection extends Thread {

    //서버통신관련 변수
    private final String SERVER_IP = "172.30.1.58";//서버의 아이피 주소
    private int port = 5050;//사용할 포트넘버
    //서버통신관련 객체
    private String Result = ""; //서버에서 날라온 결과를 저장하는 String
    private Socket socket; //통신을 위한 소켓
    private BufferedReader reader;//데이터 수신객체
    private BufferedWriter writer;//데이터 전송객체

    //사용자 정보 객체
    private String id; //사용자의 ID 정보를 저장할 공간
    private String password; //사용자의 Password 를 저장할 공간

    private ILoginCallback callback; //로그인 성공유무를 Login Activity 로 알릴 callback 객체


    public ServerConnection(String id, String password, ILoginCallback callback) {
        this.id = id;
        this.callback = callback;
        this.password = password;
    }

    @Override
    public void run() {

        try {
            settingSocket(); //서버 연결
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendData();//데이터 보내기

        //데이터 받는 내부 스레드
        Thread ReceiveData = new Thread() {
            @Override
            public void run() {
                try {
                    String line;
                    while (true) {
                        line = reader.readLine();
                        Result = line;
                        if ((Result.equals("true")) || (Result.equals("false")))
                            break; //true 나 false 가 돌아왔을 때는 서버와 통신이 되었다는 결과 (왜? 서버에서 그러도록 내가 짰으니까 ㅋ
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Result != null) { //결과값이 돌아왓을 때
                    if (Result.equals("true"))//인증성공
                        callback.StartService();
                    else if (Result.equals("false"))//인증실패(ID나 PASSWORD 중 하나가 잘못됨)
                        callback.FailToLogin();
                } else {//DB에 없는 데이터일 경우
                    callback.FailToLogin();
                }
            }
        };
        ReceiveData.start(); //데이터 받아오기

    }


    private void sendData() {
        PrintWriter out = new PrintWriter(writer, true);
        out.println(id + "/" + password + "/");
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
