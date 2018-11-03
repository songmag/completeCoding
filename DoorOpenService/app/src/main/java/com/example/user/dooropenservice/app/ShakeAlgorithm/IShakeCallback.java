package com.example.user.dooropenservice.app.ShakeAlgorithm;

public interface IShakeCallback {
    void registerListener();
    void removeListener();
    boolean isListenerSet();
}
