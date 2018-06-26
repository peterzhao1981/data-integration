package com.mode.checkProduct.userinfo;

public class CurrentUserService {
    public static User getCurrentUser() {
        User user = new User();
        user.setUserName("admin");
        user.setUserSex("男");
        return user;
    }

}
