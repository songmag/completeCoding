package com.example.user.dooropenservice.app.Server.ServerCallbackInterface;

import javax.security.auth.callback.Callback;

public interface ILogoutCallback extends Callback {
    void ServerError();
}
