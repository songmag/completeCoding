package com.example.user.dooropenservice.app.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.dooropenservice.R;
import com.example.user.dooropenservice.app.ServerConnection.ILoginCallback;
import com.example.user.dooropenservice.app.ServerConnection.ServerConnection;

/*
로그인동작과 서버통신이 이루어지는 Activity Class
여기서는 Service 가 동작하지 않는다.
@Author : 조재영
 */
public class LoginActivity extends Activity {
    private EditText ID, PassWord;
    private Button Login;
    private ServerConnection serverConnection;//서버와 연결하기위한 객체
    private ILoginCallback callback;//로그인 상황에 따른 콜백을 정의해주는 인터페이스 객체
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //로그인을 위한 콜백함수 구현
        callback = new ILoginCallback() {
            @Override
            public void StartService() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
            @Override
            public void FailToLogin(){
                Log.e("FailToLogin","로그인에 실패하였습니다.");
                /*
                 * 회원가입 프로토콜 작성
                 * 토스트 띄울 수 없음...
                 */
            }
        };


        ID = findViewById(R.id.id);
        PassWord = findViewById(R.id.password);
        Login = findViewById(R.id.button);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserID = ID.getText().toString();
                String UserPassword = PassWord.getText().toString();
                if(UserID.equals("")){
                    Toast.makeText(getApplicationContext(),"아이디를 입력하시오",Toast.LENGTH_SHORT).show();
                }
                else if(UserPassword.equals("")){
                    Toast.makeText(getApplicationContext(),"비밀번호를 입력하시오",Toast.LENGTH_SHORT).show();
                }
                else {
                    serverConnection = new ServerConnection(UserID, UserPassword, callback);
                    serverConnection.setName("ServerConnectionThread");
                    serverConnection.start();
                }
                /*
                로그인 인증 프로토콜 코드 작성
                 */



            }
        });
    }


}
