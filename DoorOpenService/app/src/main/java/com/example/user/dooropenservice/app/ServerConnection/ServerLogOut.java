package com.example.user.dooropenservice.app.ServerConnection;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class ServerLogOut extends ServerConnection {

    protected BufferedWriter writer;//데이터 전송객체
    JSONObject userID;

    public ServerLogOut(JSONObject userID) {
        super(null);
        this.userID=userID;

    }

    @Override
    public void run() {
        super.run();
        if (writer != null) {
            sendData();//데이터 보내기
        }
        try {
            if(writer!=null)
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void sendData() {
        PrintWriter out = new PrintWriter(writer, true);
        out.println(userID);
    }
    @Override
    protected void settingSocket(){
        super.settingSocket();
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
