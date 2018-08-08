package com.ximuyi.akkagame.server.game.test;

import com.ximuyi.akkagame.common.User;
import com.ximuyi.akkagame.common.extension.CommandCmd;
import com.ximuyi.akkagame.common.extension.RequestExtension;
import com.ximuyi.akkagame.server.proto.ProtoTest;
import com.ximuyi.akkaserver.extension.ICommand;

public class TestExtension extends RequestExtension {

    public static final short ID = (short)1;

    public TestExtension(short extension) {
        super(extension);
    }

    @Override
    protected void onInit() {
        super.onInit();
    }

    @CommandCmd(value = 1, threadSafe = true)
    public void test1(User user, ICommand command, ProtoTest.TestRequest message){
        ProtoTest.TestResponse.Builder builder = ProtoTest.TestResponse.newBuilder();
        builder.setCurrentDateTime(System.currentTimeMillis());
        RequestExtension.respond(user, command, builder.build());
    }
}
