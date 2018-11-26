package com.example.user.dooropenservice.app.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.dooropenservice.R;
import com.example.user.dooropenservice.app.Server.ServerConnection.ServerSignUp;
import com.example.user.dooropenservice.app.Server.UserVO;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText id, pw, pw_2, name;
    private String user_id, user_pw, user_pw2, user_name;
    private TextView company, pw_signal;

    private final int REQUEST_CODE = 100;
    private final int SERVER_ID_OK = 1000;

    private boolean CONFIRM_ID_OK = false;
    private boolean CONFIRM_PW_OK = false;
    private boolean CONFIRM_NAME_OK = false;
    private boolean CONFIRM_COMPANY_OK = false;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SetContents();

    }

    //영문,숫자만 입력(한글 필터링)
    protected InputFilter filter = new InputFilter() {

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");

            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

    private void SetContents() {
        id = (EditText) findViewById(R.id.editText_id);
        id.setFilters(new InputFilter[]{filter});

        pw = (EditText) findViewById(R.id.editText_pw);
        pw.setFilters(new InputFilter[]{filter});

        pw_2 = (EditText) findViewById(R.id.editText_pw2);
        pw_2.setFilters(new InputFilter[]{filter});

        name = (EditText) findViewById(R.id.editText_name);

        company = (TextView) findViewById(R.id.company);
        pw_signal = (TextView) findViewById(R.id.pw_signal);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:   //ID 확인하는 과정
                user_id = id.getText().toString();

                if (user_id.length() < 4 || user_id.length() > 16)
                    Toast.makeText(getApplicationContext(), "아이디를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
                else {
                    //서버에서 ID 중복확인 받는 코드 , RSULT_CODE 받아야됨
                    Toast.makeText(getApplicationContext(), user_id + "/" + "서버 접근, ID 중복확인", Toast.LENGTH_LONG).show();
                    CONFIRM_ID_OK = true;
                    //

                    if (SERVER_ID_OK == 1000) {
                        Toast.makeText(getApplicationContext(), "사용가능한 아이디 입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "입력정보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.selectCompany:
                intent = new Intent(this, SelectCompanyActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;

            case R.id.finishBtn:

                if (checkSignUp()) {
                    /*
                    UserVO user; //UserVo만들어서 여기다가 회원데이터 다 꼬라박기
                    ServerSignUp serverSignUp = new ServerSignUp(UserVo,Callback); //콜백은 아직 미구현
                    */
                    Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "입력정보를 확인해주세요", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            String result = data.getStringExtra("compName");
            company.setText(result);
        }
    }

    //스레드 써야됨
    private void checkPassword() {
        user_pw = pw.getText().toString();
        user_pw2 = pw_2.getText().toString();

        if (user_pw.length() < 6 || user_pw.length() > 16) {
            pw_signal.setText("형식이 맞지 않습니다.");
            pw_signal.setTextColor(Color.parseColor("#FF8888"));
        } else if (user_pw.equals(user_pw2)) {
            CONFIRM_PW_OK = true;
            pw_signal.setText("사용가능");
            pw_signal.setTextColor(Color.parseColor("#99FF99"));
        } else {
            pw_signal.setText("비밀번호가 일치하지 않습니다.");
            pw_signal.setTextColor(Color.parseColor("#FF8888"));
        }
    }

    private void checkUserName() {
        user_name = name.getText().toString();

        if (user_name != null) {
            CONFIRM_NAME_OK = true;
        }
    }

    private void checkCompName() {
        company.getText().toString();

        if (company != null) {
            CONFIRM_COMPANY_OK = true;
        }
    }

    private boolean checkSignUp() {
        checkPassword();
        checkUserName();
        checkCompName();

        if (CONFIRM_NAME_OK && CONFIRM_ID_OK && CONFIRM_PW_OK && CONFIRM_COMPANY_OK)
            return true;
        else
            return false;
    }
}