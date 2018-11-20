package com.example.user.dooropenservice.app.ServerConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class ServerLogin extends ServerConnection {


    //사용자 정보 객체
    private UserVO user;
    private JSONObject Juser;

    protected String Result = ""; //서버에서 날라온 결과를 저장하는 String

    private static final int LOGIN_OK = 1;
    private static final int NO_DATA = 2;
    private static final int LOGIN_FAIL = 3;

    protected BufferedReader reader;//데이터 수신객체
    protected BufferedWriter writer;//데이터 전송객체

    public ServerLogin(UserVO user, ILoginCallback callback) {
        super(callback);
        this.user = user;

        Juser = new JSONObject();
        //JSON 데이터 삽입
        try {
            Juser.put("id", Integer.parseInt(user.getId()));
            Juser.put("password", Integer.parseInt(user.getPassword()));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) { //숫자가 아닌 문자가 들어올 경우
            callback.FailToLogin();
        }
    }

    @Override
    public void run() {
        super.run();

        if (writer != null) {
            sendData();//데이터 보내기
        }
        if (reader != null) {
            //데이터 받는 내부 스레드
            Thread ReceiveData = new Thread() {
                @Override
                public void run() {
                    //데이터 읽어들이기
                    try {
                        String line;
                        while (true) {
                            line = reader.readLine();
                            Result = line;
                            if ((Result.equals(String.valueOf(LOGIN_OK))) || (Result.equals(String.valueOf(LOGIN_FAIL)))||(Result.equals(String.valueOf(NO_DATA))))
                                break; //true 나 false 가 돌아왔을 때는 서버와 통신이 되었다는 결과 (왜? 서버에서 그러도록 내가 짰으니까 ㅋ
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //결과값에 대한 실행
                    if (Result!=null) {//결과값이 돌아왓을 때
                        int flag = Integer.parseInt(Result);
                        switch (flag){
                            case LOGIN_OK :callback.StartService();//인증성공
                                break;
                            case NO_DATA :callback.NoData();//인증실패(ID나 PASSWORD 중 하나가 잘못됨)
                                break;
                            case LOGIN_FAIL : callback.FailToLogin();//DB에 데이터가 없는 경우
                                break;

                        }

                    }

                    try {
                        reader.close();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            };
            ReceiveData.setName("ReceiveData");
            ReceiveData.start(); //데이터 받아오기
        }
    }
    @Override
    protected void sendData() {
        PrintWriter out = new PrintWriter(writer, true);
        out.println(Juser);
    }
    @Override
    protected void settingSocket(){
        super.settingSocket();

        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
