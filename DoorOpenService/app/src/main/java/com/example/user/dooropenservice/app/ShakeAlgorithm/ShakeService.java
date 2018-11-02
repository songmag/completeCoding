package com.example.user.dooropenservice.app.ShakeAlgorithm;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

import com.example.user.dooropenservice.app.BluetoothThread.BluetoothThread;


/*
Shake_Algorithm 을 관리하는 Class
@Author : 조재영
 */
public class ShakeService implements SensorEventListener, IShakeCallback {

    //객체
    private LocationHolder locationHolder; //좌표정보를 저장,관리하는 클래스
    Vibrator vibrator; //알고리즘이 제대로 동작하였을시 작동하는 vibrator 객체
    SensorManager sensorManager; //Shake 정보를 얻어올 Manager 객체

    //변수
    private float speed; //가속 스피드를 저장할 변수
    private static long lastTime = 0;// 시간차를 계산하기위한 변수
    private static final int SHAKE_THRESHOLD = 800; //비교 할 Shake 의 강도 (높을수록 강하게 둔함, 낮을수록 예민)

    //앱 정보
    Context context;

    BluetoothThread bluetoothThread = null;


    public ShakeService(Context context) {
        this.context = context;

        //Shake정보를 담을 홀더클래스 생성
        locationHolder = new LocationHolder();
        //SeosorManager가져오기
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //센서 리스너 등록
        this.registerListener();

        //ShakeService가 시작됨을 알리는 바이브레이터
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);


    }


    //센서리스너 등록 메소드
    @Override
    public void registerListener() {
        //registerListener(Listener객체,센서타입,센서속도);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }


    //SensorEvenetListener를 위해 구현된 메소드
    @Override
    public void onSensorChanged(SensorEvent event) {
        //센서의 값이 바뀔때 호출되는 메소드
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis(); // 현재시간 불러오기
            long gabOfTime = (currentTime - lastTime); //현재시간과 마지막시간의 차이

            if (gabOfTime > 300) {
                lastTime = currentTime;
                locationHolder.setCurrentLocation(event.values[0], event.values[1], event.values[2]); //현재 좌표정보 저장

                speed = Math.abs(locationHolder.calculateSubtraction()) / gabOfTime * 10000; // 현재좌표와 이전좌표의 차이를 계산

                if (speed > SHAKE_THRESHOLD) {

                    vibrator.vibrate(1000);
                    sensorManager.unregisterListener(this);//한번 감지가 되면 리스너를 제거해버림



                    /*
                    BlueTooth 연결코드 작성
                    쓰레드는 한번 사용하면 재 사용할수 없기때문에 계속 새로 생성해준다
                     */

                    bluetoothThread = new BluetoothThread(this,context);

                    bluetoothThread.start();


                }

                locationHolder.setLastLocation(event.values[0], event.values[1], event.values[2]);
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //센서의 정확도 값이 바뀔 때 호출
    }
}
