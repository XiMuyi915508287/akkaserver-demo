package com.ximuyi.game.server.game.test;

import com.ximuyi.game.common.MyUser;
import com.ximuyi.game.common.extension.CommandCmd;
import com.ximuyi.game.common.extension.Extension;
import com.ximuyi.game.server.proto.ProtoTest;
import com.ximuyi.core.command.ICommand;

public class TestExtension extends Extension {

    public static final short ID = (short)1;

    public TestExtension(short extension) {
        super(extension);
    }

    @Override
    protected void onInit() {
        super.onInit();
    }

    @CommandCmd(value = 1)
    public void test1(MyUser user, ICommand command, ProtoTest.TestRequest message){
        ProtoTest.TestResponse.Builder builder = ProtoTest.TestResponse.newBuilder();
        builder.setCurrentDateTime(System.currentTimeMillis());
        Extension.userResponse(user, command, builder.build());
    }
}
