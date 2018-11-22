package com.example.user.dooropenservice.app.ServerConnection;

public class UserDetailVO extends UserVO {
    private String company;
    private String name;

    public UserDetailVO(String id, String password) {
        super(id, password);
    }


    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
