package com.example.user.dooropenservice.app.Server.ServerCallbackInterface;

import javax.security.auth.callback.Callback;

/*
 * ILoginCallback
 * 로그인동작을 관리할 Callback 메소드를 가진 인터페이스
 * function : 서비스 실행, 로그인 실패, 데이터 없음
 * 상호작용 : ServerLogin(동작결정) , LoginActivity(동작구현)
 * @Author : 조재영
 */
public interface ILoginCallback extends Callback {
    void StartService();
    void FailToLogin();
    void NoData();
    void ServerConnectionError();
}
