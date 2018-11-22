package com.example.user.dooropenservice.app.ServerConnection;

import java.net.InetSocketAddress;
import java.net.Socket;

/*
 * ServerConnection Thread
 * 서버와 연결을 위한 스레드 클래스
 * 자식으로 ServerLogOut, ServerLogin을 가진다.
 * function : 서버와의 소켓연결
 * @Author : 조재영
 */
abstract class ServerConnection extends Thread {

    //서버통신관련 변수
    private final String SERVER_IP = "221.146.111.40";//서버의 아이피 주소(제섭이형네 아이피주소)
//    private final String SERVER_IP = "210.205.46.5";//우리집(재영이집 아이피주소)
//    private final String SERVER_IP = "169.254.154.205";//변경되는 IP
    private int port = 5050;//사용할 포트넘버

    //서버통신관련 객체
    protected Socket socket; //통신을 위한 소켓


    //로그인 성공유무를 Login Activity 로 알릴 callback 객체
    protected ILoginCallback callback;

    public ServerConnection(ILoginCallback callback) {
        this.callback = callback;

    }

    @Override
    public void run(){
        settingSocket();
    }
    protected abstract void sendData();


    protected void settingSocket() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(SERVER_IP, port), 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}