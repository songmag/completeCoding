package com.example.user.dooropenservice.app.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.dooropenservice.R;
import com.example.user.dooropenservice.app.ServerConnection.ILoginCallback;
import com.example.user.dooropenservice.app.ServerConnection.ServerLogin;
import com.example.user.dooropenservice.app.ServerConnection.UserVO;

/*
로그인동작과 서버통신이 이루어지는 Activity Class
여기서는 Service 가 동작하지 않는다.
@Author : 조재영
 */
public class LoginActivity extends Activity {


    private EditText ID, PassWord;
    private Button Login;
    private ServerLogin serverLogin;//서버와 연결하기위한 객체
    private ILoginCallback callback;//로그인 상황에 따른 콜백을 정의해주는 인터페이스 객체
    private UserVO user;

    private String id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences preferences = getSharedPreferences("LoginInfo",0);
        id = preferences.getString("id","");
        if(!id.equals("")){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        //위치정보 허가받기 (RunTime Permission Check)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        //로그인을 위한 콜백함수 구현
        callback = new ILoginCallback() {
            @Override
            public void StartService() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                SharedPreferences preferences = getSharedPreferences("LoginInfo",0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("id",ID.getText().toString());
                editor.commit();

                startActivity(intent);
            }

            @Override
            public void FailToLogin() {
                Log.e("FailToLogin", "로그인에 실패하였습니다.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void NoData() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "사용자 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };


        ID = findViewById(R.id.id);
        PassWord = findViewById(R.id.password);
        Login = findViewById(R.id.button);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = new UserVO(ID.getText().toString(), PassWord.getText().toString()); //사용자 정보 저장
                if (user.getId().equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력하시오", Toast.LENGTH_SHORT).show();
                }
                if (user.getPassword().equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하시오", Toast.LENGTH_SHORT).show();
                }
                if (!user.getId().equals("") && !user.getPassword().equals("")) {//둘다 데이터가 있는 경우 시작
                    serverLogin = new ServerLogin(user,callback);
                    serverLogin.setName("ServerConnectionThread");
                    serverLogin.start();
                }
                /*
                로그인 인증 프로토콜 코드 작성
                 */


            }
        });
    }


}
