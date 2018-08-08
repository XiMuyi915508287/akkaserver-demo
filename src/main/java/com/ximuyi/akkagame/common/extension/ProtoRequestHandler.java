package com.ximuyi.akkagame.common.extension;

import com.google.protobuf.MessageLite;
import com.ximuyi.akkagame.common.User;
import com.ximuyi.akkaserver.IUser;
import com.ximuyi.akkaserver.extension.ICommand;
import com.ximuyi.akkaserver.extension.request.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtoRequestHandler extends CommandHandler<MessageLite> {

    private static final Logger logger = LoggerFactory.getLogger(ProtoRequestHandler.class);

    private final IMethodInvoker invoker;
    private boolean isThreadSafe = false;

    public ProtoRequestHandler(ICommand extCmd, IMethodInvoker invoker) {
        super(extCmd);
        this.invoker = invoker;
    }

    public void setThreadSafe(boolean value) {
        isThreadSafe = value;
    }

    @Override
    protected void handleCommand(IUser extUser, MessageLite message) throws Throwable {
        logger.debug("userId={}, account={}, cmd={} message={{}}", extUser.getUserId(), extUser.getAccount(), getCmd().getUniqueId(), message.toString());
        invoker.invoke(getCmd(), (User) extUser, message);
    }

    @Override
    public boolean isThreadSafe() {
        return isThreadSafe;
    }
}
