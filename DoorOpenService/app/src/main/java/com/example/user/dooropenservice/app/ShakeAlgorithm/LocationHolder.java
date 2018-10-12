package com.example.user.dooropenservice.app.ShakeAlgorithm;
/*
Shake_Algorithm 의 좌표정보를 저장/관리 하는 클래스
@Author : 조재영
 */
public class LocationHolder {
    private float lastX, lastY, lastZ;
    private float x, y, z;

    public LocationHolder() {
        lastZ = 0;
        lastY = 0;
        lastX = 0;
        x = 0;
        y = 0;
        z = 0;
    }

    public void setCurrentLocation(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setLastLocation(float x, float y, float z) {
        lastX = x;
        lastY = y;
        lastZ = z;
    }

    public float calculateSubtraction() {
        float result = x + y + z - (lastX + lastY + lastZ);
        return result;
    }
}