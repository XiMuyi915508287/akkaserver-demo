package com.ximuyi.akkagame.server.game.user;

import com.ximuyi.akkagame.common.User;
import com.ximuyi.akkagame.server.proto.ProtoUser;
import com.ximuyi.akkaserver.IUser;
import com.ximuyi.akkaserver.coder.ResponseCode;
import com.ximuyi.akkaserver.extension.request.LoginHanlder;

public class MyLoginHanlder extends LoginHanlder<ProtoUser.LoginRequest, ProtoUser.LoginResponse> {

    public MyLoginHanlder() {
    }

    @Override
    protected LoginResult checkLogin(ProtoUser.LoginRequest messsage) {
        ProtoUser.LoginResponse response = ProtoUser.LoginResponse.newBuilder().build();
        return new LoginResult(buildExtUser(messsage.getUserId()), ResponseCode.SUCCESS, response);
    }


    private IUser buildExtUser( long userId){
        User user = new User( userId, "account-" + userId);
        user.setLogin(true);
        return user;
    }
}
