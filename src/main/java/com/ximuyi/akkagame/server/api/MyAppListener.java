package com.ximuyi.akkagame.server.api;

import com.ximuyi.akkagame.common.extension.ReqExtensionManager;
import com.ximuyi.akkagame.server.game.user.MyHeartBeatHandler;
import com.ximuyi.akkagame.server.game.user.MyLoginHanlder;
import com.ximuyi.akkagame.server.proto.ProtoUser;
import com.ximuyi.akkaserver.api.AppListener;
import com.ximuyi.akkaserver.config.ConfigWrapper;

public class MyAppListener implements AppListener {

    @Override
    public void onInit(ConfigWrapper config) throws Throwable {
        //请求处理的命令
        ReqExtensionManager.init(config);
        ReqExtensionManager.register(new MyLoginHanlder(), ProtoUser.LoginRequest.getDefaultInstance());
        ReqExtensionManager.register(new MyHeartBeatHandler(), ProtoUser.HeartBeatRequest.getDefaultInstance());
    }

    @Override
    public void onLaunch() {
    }

    @Override
    public void onShutdown() {
    }
}
