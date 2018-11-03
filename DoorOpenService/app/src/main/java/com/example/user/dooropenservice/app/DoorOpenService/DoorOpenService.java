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
        shakeService = new ShakeService(this);
        setNotification();//백그라운드 서비스를 유지하기위한 설정
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!shakeService.isListenerSet()){
            shakeService.registerListener();
        }

        return Service.START_STICKY;
    }

    //Notification을 추가함으로써 ForeGround에 Service가 실행되고있음을 보임으로써 Service 종료 방지
    public void setNotification(){
        Notification.Builder builder = new Notification.Builder(this).setSmallIcon(R.mipmap.sejong).setContentText("앙 기모띠");
        startForeground(DOOR_OPEN_SERVICE_ID,builder.build());
    }

}
