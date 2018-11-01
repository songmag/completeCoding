package com.example.user.dooropenservice.app.DoorOpenService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.user.dooropenservice.app.ShakeAlgorithm.ShakeService;

public class DoorOpenService extends Service {


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
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
