package com.example.user.dooropenservice.app.DoorOpenService;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        shakeService = new ShakeService(getApplicationContext());
        setNotification();
        return Service.START_STICKY;
    }
    public void setNotification(){
        Notification.Builder builder = new Notification.Builder(this).setSmallIcon(R.mipmap.sejong).setContentText("Background 동작중 ^_^");
        startForeground(DOOR_OPEN_SERVICE_ID,builder.build());
    }
    @Override
    public void onCreate() {


        super.onCreate();
    }
}
