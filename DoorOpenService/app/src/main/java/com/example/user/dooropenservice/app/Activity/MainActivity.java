package com.example.user.dooropenservice.app.Activity;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.user.dooropenservice.R;
import com.example.user.dooropenservice.app.DoorOpenService.DoorOpenService;
/*
어플에대한 설명과 Service들이 실행되는 메인 엑티비티 클래스
@Author : 조재영
 */
public class MainActivity extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //블루투스 이용 가능상태 확인
        CheckingBluetoothState();

        //DoorOpenService 실행
        Intent intent = new Intent(getApplicationContext(), DoorOpenService.class);

//        startForegroundService(intent);
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
