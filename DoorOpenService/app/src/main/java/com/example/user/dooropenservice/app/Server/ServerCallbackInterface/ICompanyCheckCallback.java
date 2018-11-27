package com.example.user.dooropenservice.app.Server.ServerCallbackInterface;

import com.example.user.dooropenservice.app.Server.CompanyVO;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

public interface ICompanyCheckCallback extends Callback {
    void startService(ArrayList<CompanyVO> companyVOArrayList);
}
