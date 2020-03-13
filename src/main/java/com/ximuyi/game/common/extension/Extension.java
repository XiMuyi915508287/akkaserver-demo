package com.ximuyi.game.common.extension;

import java.lang.reflect.Method;
import java.util.Collection;

import com.google.protobuf.MessageLite;
import com.ximuyi.core.coder.ResultCode;
import com.ximuyi.core.CoreAccessor;
import com.ximuyi.core.command.Command;
import com.ximuyi.core.command.ICommand;
import com.ximuyi.core.command.handler.CommandHandlerUtil;
import com.ximuyi.core.io.IOUtil;
import com.ximuyi.core.user.IUser;
import com.ximuyi.game.common.MyUser;
import com.ximuyi.game.server.coder.MyMessageCoder;

public abstract class Extension {

    private final Class<?>[] CLS_TYPES = new Class[]{MyUser.class, ICommand.class, MessageLite.class};

    private final short extension;

    public Extension(short extension) {
        this.extension = extension;
        registerRequestAndProto();
    }

    public final void init(){
        onInit();
    }

    protected void onInit(){

    }

    protected void onDestroy(){

    }

    private void registerRequestAndProto() {
        Method[] methods = this.getClass().getDeclaredMethods();
        final Object myself = this;
        for (Method method : methods) {
            CommandCmd cmd = method.getAnnotation(CommandCmd.class);
            if (cmd == null){
                continue;
            }
            Class<?>[] classes = method.getParameterTypes();
            if (classes.length != CLS_TYPES.length){
                throw new UnsupportedOperationException(method.getName());
            }
            for (int i = 0; i < CLS_TYPES.length; i++) {
                if (!CLS_TYPES[i].isAssignableFrom(classes[i])){
                    throw new UnsupportedOperationException(classes[i].getName());
                }
            }
            ICommand command = new Command(extension, cmd.value());
            ProtoCommandHandler handler = new ProtoCommandHandler(command, (command1, user, message) -> {
                method.invoke(myself, user, command1, message);
            });
            CommandHandlerUtil.reload(handler);
            try {
                MessageLite messageLite = (MessageLite)classes[classes.length - 1].getMethod("getDefaultInstance").invoke(null);
                CoreAccessor.getInstance().getManager(MyMessageCoder.class).register(command, messageLite);
            }
            catch (Exception e) {
                throw new RuntimeException("getDefaultInstance");
            }
        }
    }

    public static final void userResponse(MyUser user, ICommand command, ResultCode resultCode, MessageLite messageLite){
        IOUtil.userResponse(user, command, resultCode, messageLite);
    }

    public static final void userResponse(MyUser user, ICommand command, MessageLite messageLite){
        userResponse(user, command, ResultCode.SUCCESS, messageLite);
    }

    public static final void sharedUserResponse(Collection<? extends IUser> users, ICommand command, ResultCode resultCode, MessageLite messageLite){
        IOUtil.sharedUserResponse(users, command, resultCode, messageLite);
    }


    public static final void sharedUserResponse(Collection<? extends IUser> users, ICommand command, MessageLite messageLite){
        sharedUserResponse(users, command, ResultCode.SUCCESS, messageLite);
    }
}
