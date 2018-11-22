package com.example.user.dooropenservice.app.Activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.dooropenservice.R;
import com.example.user.dooropenservice.app.DoorOpenService.DoorOpenService;
import com.example.user.dooropenservice.app.ServerConnection.ServerLogOut;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * MainActivity
 * 어플에대한 설명과 로그아웃기능이 있는 메인 엑티비티 클래스
 * function : LoginActivity에서 받은 정보를 가져와 로그아웃정보로 사용한다(SharedPreference)
 * 상호작용 : LoginActivity , ServerLogOut
 * @Author : 조재영
 */
public class MainActivity extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;

    Button logOutBtn;//로그아웃 버튼

    ServerLogOut serverLogOut;//서버에서 flag 를 바꾸기 위한 로그아웃 스레드

    JSONObject userID;//현재 사용중인 사용자 ID를 담을 JsonObject;
    String id;//사용자 id

    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //로그인정보를 가져오는 작업
        preferences = getSharedPreferences("LoginInfo",0);
        id = preferences.getString("id","");



        //로그아웃버튼
        logOutBtn = findViewById(R.id.logout);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID = new JSONObject();
                //JSON 데이터 삽입
                try {
                    userID.put("id", Integer.parseInt(id));
                    userID.put("password",null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                serverLogOut = new ServerLogOut(userID);
                serverLogOut.setName("serverLogout");
                serverLogOut.start();
                //로그아웃을 하면 정보를 지운다
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("id");
                editor.commit();
                stopService(new Intent(getApplicationContext(),DoorOpenService.class));
                finish();
            }
        });
        //블루투스 이용 가능상태 확인
        CheckingBluetoothState();

        //DoorOpenService 실행
        Intent intent = new Intent(getApplicationContext(), DoorOpenService.class);
        startService(intent);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishAffinity();
    }
    private void CheckingBluetoothState() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
            finish(); //서비스 종료
        }
        //블루투스 사용상태가 아닐경우 블루투스 사용상태 Dialog 출력
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            startActivity(enableIntent);
        }
    }

}
