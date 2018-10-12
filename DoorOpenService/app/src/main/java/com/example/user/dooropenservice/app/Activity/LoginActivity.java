package com.example.user.dooropenservice.app.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.dooropenservice.R;
/*
로그인동작과 서버통신이 이루어지는 Activity Class
여기서는 Service 가 동작하지 않는다.
@Author : 조재영
 */
public class LoginActivity extends Activity {
    private EditText ID,PassWord;
    private Button Login;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ID = findViewById(R.id.id);
        PassWord = findViewById(R.id.password);
        Login  = findViewById(R.id.button);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                로그인 인증 프로토콜 코드 작성
                 */
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }
        });
    }
}
