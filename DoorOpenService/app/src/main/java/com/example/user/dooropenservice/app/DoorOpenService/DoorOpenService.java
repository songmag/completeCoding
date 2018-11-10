package com.example.user.dooropenservice.app.DoorOpenService;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.user.dooropenservice.R;
import com.example.user.dooropenservice.app.ShakeAlgorithm.ShakeService;

public class DoorOpenService extends Service {

    static final int DOOR_OPEN_SERVICE_ID = 1;
    ShakeService shakeService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onCreate() {

        shakeService = new ShakeService(this);//이거를 범위 내에 들어왔을떄 만듬
        setNotification();//백그라운드 서비스를 유지하기위한 설정 (이거는 유지 onCreate에 있어야 함)
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!shakeService.isListenerSet()){
            shakeService.registerListener(); //거리 밖으로 오면 이코드 추가 3줄 다.
        }

        return Service.START_STICKY;
    }

    //Notification을 추가함으로써 ForeGround에 Service가 실행되고있음을 보임으로써 Service 종료 방지
    public void setNotification(){
        Notification.Builder builder = new Notification.Builder(this).setSmallIcon(R.mipmap.sejong).setContentText("앙 기모띠");
        startForeground(DOOR_OPEN_SERVICE_ID,builder.build());
    }

}
