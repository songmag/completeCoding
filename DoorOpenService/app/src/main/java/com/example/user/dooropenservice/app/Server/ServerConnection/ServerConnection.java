package com.example.user.dooropenservice.app.Server.ServerConnection;

import com.example.user.dooropenservice.app.Server.AES256Util;
import com.example.user.dooropenservice.app.Server.ServerCallbackInterface.ILoginCallback;
import com.example.user.dooropenservice.app.Server.ServerCallbackInterface.ILogoutCallback;
import com.example.user.dooropenservice.app.Server.UserVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.security.auth.callback.Callback;

/*
 * ServerConnection Thread
 * 서버와 연결을 위한 스레드 클래스
 * 자식으로 ServerLogOut, ServerLogin을 가진다.
 * function : 서버와의 소켓연결
 * @Author : 조재영
 */
abstract class ServerConnection extends Thread {

    //서버통신관련 변수
//    private final String SERVER_IP = "221.146.111.40";//서버의 아이피 주소(제섭이형네 아이피주소)
//    private final String SERVER_IP = "210.205.46.5";//우리집(재영이집 아이피주소)
    private final String SERVER_IP = "172.16.0.229";//변경되는 IP
    private int port = 5050;//사용할 포트넘버

    //서버통신관련 객체
    protected Socket socket; //통신을 위한 소켓
    private static final String KEY = "ase256-run-key!!!";

    //로그인 성공유무를 Login Activity 로 알릴 callback 객체
    protected Callback callback;

    //사용자 정보를 담을 UserVo 객체
    UserVO user;

    //JsonParising을 위한 Json객체
    private JSONObject Juser;

    public ServerConnection(UserVO user,Callback callback) {
        this.user=  user;
        this.callback = callback;
        Juser = new JSONObject();
        //JSON 데이터 삽입
        try {
            String encryptionPassword = getEncryption(user);//암호화
            Juser.put("id", user.getId());
            Juser.put("password", encryptionPassword);
            Juser.put("company",user.getCompany());
            Juser.put("name",user.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getEncryption(UserVO user) throws Exception{
        String encryptionPassword = null;
        AES256Util util = new AES256Util(KEY);
        if(user.getPassword()!=null) {
            encryptionPassword = util.aesEncode(user.getPassword());
        }
        return encryptionPassword;
    }
    @Override
    public void run() {
        settingSocket();
    }

    protected abstract void sendData();


    protected void settingSocket() {

        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(SERVER_IP, port), 2000);
        } catch (IOException e) {
                if(callback instanceof ILogoutCallback){
                    ((ILogoutCallback)callback).ServerError(); //로그아웃시에는 callback을 넘겨주지 않으므로 체킹
                }
                else if(callback instanceof ILoginCallback){
                    ((ILoginCallback)callback).ServerConnectionError(); //서버연결오류
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

            e.printStackTrace();
        }


    }

    //하위클래스에서 사용자정보를 가진 JSonObject 를 가져오기 위함
    public JSONObject getJuser() {
        return Juser;
    }
}
