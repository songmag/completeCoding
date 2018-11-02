package com.example.user.dooropenservice.app.BluetoothThread;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;

import com.example.user.dooropenservice.app.ShakeAlgorithm.IShakeCallback;
import com.example.user.dooropenservice.app.ShakeAlgorithm.ShakeService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/*
    BlueTooth를 전체적으로 관리하는 서비스 클래스
    ShakeService가 종료되며 시작된다

 */
public class BluetoothThread extends Thread {
    //블루투스 통신을 위한 UUID정보
    final String base_UUID = "00001101-0000-1000-8000-00805F9B34FB"; //Base UUID
    UUID uuid = UUID.fromString(base_UUID); //UUID를 고유값으로 설정

    //안드로이드 어플리케이션 점보
    Context context;

    // 블루투스 관련 객체
    BluetoothDevice RemoteDevice; //아두이노와 연결될 객체
    Set<BluetoothDevice> device; //Bluetooth의 페어링 된 정보를 가지는 Set
    String arduino = ""; //사용할 아두이노 이름정보를 저장할 공간
    BluetoothAdapter bluetoothAdapter;


    //통신용 변수
    BluetoothSocket socket = null;
    InputStream inputStream;
    OutputStream outputStream;

    //수신 전용 변수
    byte[] readBuffer;
    int readBufferPosition;
    byte responseResult = 100;
    Thread mWorkerThread = null;


    //콜백을 위한 객체
    IShakeCallback shakeCallback;


    @Override
    public void run() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothStart();
    }

    @Override
    public void interrupt() {

        if (inputStream != null || outputStream != null) {
            try {
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (mWorkerThread != null) {
                mWorkerThread = null;
            }
        }
    }

    public BluetoothThread(ShakeService shakeService,Context context) {
        //callback메소드를 쓰기위한 객체 정보 얻어오기
        shakeCallback = shakeService;
        this.context = context;


    }


    /*
     * Bluetooth Adapter에대한 셋팅을 하는 메소드
     * 어댑터를 설정하고, 기기가 블루투스 사용가능상태인지 아닌지 검사한다.
     * 페어링 된 장치가 있으면 선택된 디바이스로 연결을 시도한다.
     */
    private void BluetoothStart() {

        //블루투스를 지원하며 활성상태일 경우 페어링된 기기 목록을 가져와 아두이노를 연결
        device = bluetoothAdapter.getBondedDevices();

        if (device.size() > 0) { //페어링 된 장치가 있는 경우
            selectDevice();

            connectToSelectedDevices(arduino);

        } else { //페어링 된 장치가 없는 경우
            Toast.makeText(context,"페어링 된 장치가 없습니다.",Toast.LENGTH_SHORT).show();
            shakeCallback.registerListener();
            this.interrupt();
        }

    }


    /*
       블루투스 기기를 List에 집어넣고
       페어링 된 기기가 있으면 우리가 사용할 기기정보를 저장해주는 메소드
     */
    private void selectDevice() {


        //페어링 된 블루투스 장치의 이름을 저장
        List<String> list = new ArrayList<String>();
        for (BluetoothDevice searchDevice : device) {
            list.add(searchDevice.getName());
        }

        /*
        # 다이얼로그를 띄워서 선택된 디바이스정보를 저장하는 로직 작성필요
        arduino에 저장
         */
        if (list.size() > 0)
            for (String selectedDevice : list) {
                if (selectedDevice.contains("HC-06")) { //"HC-06"을 포함하고 있으면 변수에 저장
                    arduino = selectedDevice.toString();

                }
            }


    }


    /*
     디바이스의 이름을 찾아 연결을 시도하는 메소드
     */
    void connectToSelectedDevices(String selectedDeviceName) {
        RemoteDevice = getDeviceFromBondedList(selectedDeviceName);

        try {
            // 소켓 생성
            socket = RemoteDevice.createRfcommSocketToServiceRecord(uuid);

            // RFCOMM 채널을 통한 연결
            socket.connect();

            // 데이터 송수신을 위한 스트림 열기
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();

            // 데이터 송신
            sendData("1");

            // 데이터 수신 준비
            beginListenForData();


        } catch (Exception e) {
            // 블루투스 연결 중 오류 발생

        }
    }


    /*
    데이터 전송 함수
     */
    void sendData(String msg) throws Exception {

        outputStream.write(msg.getBytes());    // 문자열 전송


    }

    /*
    원하는 기기가 리스트에 존재하면 그 디바이스 정보를 가져오는 메소드
    @return 선택된 디바이스
     */
    BluetoothDevice getDeviceFromBondedList(String name) {
        BluetoothDevice selectedDevice = null;

        for (BluetoothDevice searchDevice : device) {
            if (name.equals(searchDevice.getName())) {
                selectedDevice = searchDevice;
                break;
            }
        }
        return selectedDevice;
    }

    // 데이터 수신(쓰레드 사용 수신된 메시지를 계속 검사함)
    void beginListenForData() {


        readBufferPosition = 0;                 // 버퍼 내 수신 문자 저장 위치.
        readBuffer = new byte[1024];            // 수신 버퍼.

        // 문자열 수신 쓰레드.
        mWorkerThread = new DataReceiveThread(this);
        mWorkerThread.start();

    }

    /*
     * 내부 데이터 수신 쓰레드
     */
    private class DataReceiveThread extends Thread {
        Thread thread;//BlueTooth Thread의 정보를 가져올 객체

        public DataReceiveThread(BluetoothThread thread) {
            this.thread = thread;
        }

        @Override
        public void interrupt() {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            // interrupt() 메소드를 이용 스레드를 종료시키는 예제이다.
            // interrupt() 메소드는 하던 일을 멈추는 메소드이다.
            // isInterrupted() 메소드를 사용하여 멈추었을 경우 반복문을 나가서 스레드가 종료하게 된다.
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // InputStream.available() : 다른 스레드에서 blocking 하기 전까지 읽은 수 있는 문자열 개수를 반환함.
                    int byteAvailable = inputStream.available();   // 수신 데이터 확인
                    if (byteAvailable > 0) {                        // 데이터가 수신된 경우.
                        byte[] packetBytes = new byte[byteAvailable];
                        // read(buf[]) : 입력스트림에서 buf[] 크기만큼 읽어서 저장 없을 경우에 -1 리턴.
                        inputStream.read(packetBytes);
                        for (int i = 0; i < byteAvailable; i++) {
                            byte b = packetBytes[i];
                            if (b == responseResult) {//결과값이 같으면
//                                    byte[] encodedBytes = new byte[readBufferPosition];
                                sendData("0");//불끄기 신호 전달
                                readBufferPosition = 0;

                                //ShakeService 리스너 재등록 (CallBack 이용)
                                shakeCallback.registerListener();


                                //현재 쓰레드에 인터럽트를 걸어줌
                                Thread.currentThread().interrupt();
                                thread.interrupt();

                            } else {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }
                    }

                } catch (Exception e) {    // 데이터 수신 중 오류 발생.
                    shakeCallback.registerListener();
                }
            }
        }

    }

}
