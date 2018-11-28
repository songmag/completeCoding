package com.example.user.dooropenservice.app.Server.ServerConnection;

import com.example.user.dooropenservice.app.Model.UserVO;
import com.example.user.dooropenservice.app.Server.AES256Util;
import com.example.user.dooropenservice.app.Server.ServerCallbackInterface.ILogoutCallback;
import com.example.user.dooropenservice.app.Server.ServerCallbackInterface.IServerCallback;

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
 * function : 서버와의 소켓연결, UserVO 를 JsonObject 로 묶는다.
 * @Author : 조재영
 */
abstract class ServerConnection extends Thread {

    //서버통신관련 변수
    private final String SERVER_IP = "221.146.111.40";//서버의 아이피 주소(제섭이형네 아이피주소)
    //    private final String SERVER_IP = "210.205.46.5";//우리집(재영이집 아이피주소)
//    private final String SERVER_IP = "172.16.20.37";//변경되는 IP
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

    private final int LOGIN = 1;
    private final int LOGOUT = 2;
    private final int COMPANY_CHECK = 3;
    private final int DUPLICATE_ID = 4;
    private final int SIGN_UP = 5;

    public ServerConnection(UserVO user, Callback callback) {
        this.user = user;
        this.callback = callback;
        Juser = new JSONObject();
        //JSON 데이터 삽입
        try {
            String encryptionPassword = getEncryption(user);//암호화

            //해당하는 작업을 알려줄 Key값 설정
            int key = 0;
            if (this instanceof ServerLogin) {
                key = LOGIN;
            } else if (this instanceof ServerLogOut) {
                key = LOGOUT;
            } else if (this instanceof ServerCompanyCheck) {
                key = COMPANY_CHECK;
            } else if (this instanceof ServerDuplicateID) {
                key = DUPLICATE_ID;
            } else if (this instanceof ServerSignUp) {
                key = SIGN_UP;
            }
            Juser.put("key", key);
            Juser.put("id", user.getId());
            Juser.put("password", encryptionPassword);
            Juser.put("company", user.getCompany());
            Juser.put("name", user.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    private String getEncryption(UserVO user){
//        String encryptionPassword = null;
//        if(user.getPassword()!=null) {
//            encryptionPassword = SHA256.encrypt(user.getId());
//        }
//        return encryptionPassword;
//    }
    private String getEncryption(UserVO user) throws Exception {
        String encryptionPassword = null;
        AES256Util util = new AES256Util(KEY);
        if (user.getPassword() != null) {
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
            if (callback instanceof ILogoutCallback) {//로그아웃시 ServerError
                ((ILogoutCallback) callback).ServerError(); //로그아웃시에는 callback을 넘겨주지 않으므로 체킹
            } else if (callback instanceof IServerCallback) {//로그인시 ServerError
                ((IServerCallback) callback).ServerConnectionError(); //서버연결오류
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
