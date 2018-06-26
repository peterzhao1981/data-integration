package com.mode.checkProduct.userinfo;

//保存用户信息
public class User {
    private String userName = null;
    private String userSex = null;

    public String getUserSex() {
        return userSex;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
