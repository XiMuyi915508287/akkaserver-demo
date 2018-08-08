package com.ximuyi.akkagame.common;

import com.ximuyi.akkaserver.IUser;

public class User implements IUser {

    private final long userId;
    private final String account;
    private volatile boolean isLogin;

    public User(long userId, String account) {
        this.userId = userId;
        this.account = account;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
