package com.ximuyi.game.server.api;

import com.ximuyi.core.api.IAppListener;
import com.ximuyi.core.command.CommandUtil;
import com.ximuyi.core.CoreAccessor;
import com.ximuyi.game.common.extension.ExtensionManager;
import com.ximuyi.game.server.coder.MyMessageCoder;
import com.ximuyi.game.server.proto.ProtoUser;

public class MyAppListener implements IAppListener {

    @Override
    public void onInit() throws Throwable {
        //请求处理的命令
        initSystemCommand();
        ExtensionManager.init();
    }

    private void initSystemCommand(){
        MyMessageCoder coder = CoreAccessor.getInstance().getManager(MyMessageCoder.class);
        coder.register(CommandUtil.LOGIN, ProtoUser.LoginRequest.getDefaultInstance());
    }

    @Override
    public void onLaunch() {
    }

    @Override
    public void onShutdown() {
    }
}
