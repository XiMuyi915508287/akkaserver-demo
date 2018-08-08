package com.ximuyi.akkagame.common.extension;

import com.google.protobuf.MessageLite;
import com.ximuyi.akkagame.common.User;
import com.ximuyi.akkagame.common.util.ResultCode;
import com.ximuyi.akkagame.server.coder.MyMessageCoder;
import com.ximuyi.akkaserver.IUser;
import com.ximuyi.akkaserver.core.CoreAccessor;
import com.ximuyi.akkaserver.extension.Command;
import com.ximuyi.akkaserver.extension.ICommand;
import com.ximuyi.akkaserver.extension.request.RequestHandlerUtil;
import com.ximuyi.akkaserver.io.IoUtil;

import java.lang.reflect.Method;
import java.util.Collection;

public abstract class RequestExtension {

    private final Class<?>[] CLS_TYPES = new Class[]{User.class, ICommand.class, MessageLite.class};

    private final short extension;

    public RequestExtension(short extension) {
        this.extension = extension;
        registerRequestAndProto();
    }

    public final void init(){
        onInit();
    }

    protected void onInit(){

    }

    protected void onDestory(){

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
            ProtoRequestHandler handler = new ProtoRequestHandler(command, (command1, user, message) -> {
                method.invoke(myself, user, command1, message);
            });
            handler.setThreadSafe(cmd.threadSafe());
            RequestHandlerUtil.reload(handler);
            try {
                MessageLite messageLite = (MessageLite)classes[classes.length - 1].getMethod("getDefaultInstance").invoke(null);
                CoreAccessor.getLocator().getManager(MyMessageCoder.class).register(command, messageLite);
            }
            catch (Exception e) {
                throw new RuntimeException("getDefaultInstance");
            }
        }
    }

    public static final void respond(User user, ICommand command, ResultCode resultCode, MessageLite messageLite){
        IoUtil.send(user, command, resultCode.getId(), messageLite);
    }

    public static final void respond(User user, ICommand command, MessageLite messageLite){
        respond(user, command, ResultCode.SUCCESS, messageLite);
    }

    public static final void respond(Collection<? extends IUser> users, ICommand command, ResultCode resultCode, MessageLite messageLite){
        IoUtil.send(users, command, resultCode.getId(), messageLite);
    }


    public static final void respond(Collection<? extends IUser> users, ICommand command, MessageLite messageLite){
        respond(users, command, ResultCode.SUCCESS, messageLite);
    }
}
