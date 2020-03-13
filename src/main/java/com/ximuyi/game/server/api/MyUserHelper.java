package com.ximuyi.game.server.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ximuyi.core.api.login.AccountInfo;
import com.ximuyi.core.api.login.ConnectWay;
import com.ximuyi.core.api.login.IUserHelper;
import com.ximuyi.core.api.login.LoginResult;
import com.ximuyi.core.coder.ResultCode;
import com.ximuyi.core.user.IUser;
import com.ximuyi.game.common.MyUser;
import com.ximuyi.game.server.proto.ProtoUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyUserHelper implements IUserHelper<ProtoUser.LoginRequest> {

    private static final Logger logger = LoggerFactory.getLogger(MyUserHelper.class);

    private Map<Long, MyUser> userMap = new ConcurrentHashMap<>();

    @Override
    public LoginResult doUserLogin(ProtoUser.LoginRequest message, boolean isReconnect) {
        ProtoUser.LoginResponse response = ProtoUser.LoginResponse.newBuilder().build();
        MyUser user = new MyUser(message.getUserId(), "account-" + message.getUserId());
        return new LoginResult(user, ConnectWay.Inside, ResultCode.SUCCESS, response);
    }

    @Override
    public AccountInfo getAccountInfo(ProtoUser.LoginRequest message) {
        long userId = userMap.containsKey(message.getUserId()) ? message.getUserId() : 0;
        return new AccountInfo(userId, "");
    }

    @Override
    public void onReconnect(IUser extUser, ConnectWay connectWay) {

    }

    @Override
    public void onDisconnect(IUser extUser) {

    }

    @Override
    public void onUserLogin(IUser extUser) {
        userMap.put(extUser.getUserId(), (MyUser)extUser);
    }

    @Override
    public void onUserLogout(IUser extUser) {
        userMap.remove(extUser.getUserId());
    }
}
