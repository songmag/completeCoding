package com.example.user.dooropenservice.app.Server.ServerConnection;

import com.example.user.dooropenservice.app.Server.ServerCallbackInterface.ILoginCallback;
import com.example.user.dooropenservice.app.Server.ServerCallbackInterface.ISignUpCallback;
import com.example.user.dooropenservice.app.Server.UserVO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ServerSignUp extends ServerConnection {

    protected String Result = ""; //서버에서 날라온 결과를 저장하는 String

    protected BufferedReader reader;//데이터 수신객체
    protected BufferedWriter writer;//데이터 전송객체

    public static final int DUPLICATE_ID = 4; //아이디 중복
    public static final int SUCCESS = 5;//연결 성공

    public ServerSignUp(UserVO user, ISignUpCallback callback) {
        super(user,callback);
    }

    @Override
    public void run() {
        super.run();
        if(writer!=null){
            sendData();
        }
        if(reader!=null){
            Thread ReceiveData = new Thread(){
                @Override
                public void run() {
                    //데이터 읽어들이기
                    try {
                        String line;
                        while (true) {
                            line = reader.readLine();
                            Result = line;
                            if ((Result.equals(String.valueOf(DUPLICATE_ID))) || (Result.equals(String.valueOf(SUCCESS))))
                                break; //true 나 false 가 돌아왔을 때는 서버와 통신이 되었다는 결과 (왜? 서버에서 그러도록 내가 짰으니까 ㅋ
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //결과값에 대한 실행
                    if (Result!=null) {//결과값이 돌아왓을 때
                        int flag = Integer.parseInt(Result);
                        switch (flag){
                            case DUPLICATE_ID :((ILoginCallback)callback).StartService();//인증성공
                                break;
                            case SUCCESS :((ILoginCallback)callback).NoData();//인증실패(ID나 PASSWORD 중 하나가 잘못됨)
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
            ReceiveData.setName("ReceiveData with SignUp");
            ReceiveData.start();
        }

        /*
         * 서버에서 데이터를 받아
         * 1. 아이디가 있는지 없는지 체크 콜백이용 결과산출
         * 2. 성공하면 콜백이용 Dooropenservice실행
         */
    }

    @Override
    protected void sendData() {
        PrintWriter out = new PrintWriter(writer, true);
        out.println(getJuser());
    }
}
