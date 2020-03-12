package com.ximuyi.game.common.extension;

import com.google.protobuf.MessageLite;
import com.ximuyi.core.command.ICommand;
import com.ximuyi.core.command.handler.CommandHandler;
import com.ximuyi.core.user.IUserSession;
import com.ximuyi.game.common.MyUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtoCommandHandler extends CommandHandler<MessageLite> {

    private static final Logger logger = LoggerFactory.getLogger(ProtoCommandHandler.class);

    private final IMethodInvoker invoker;

    public ProtoCommandHandler(ICommand extCmd, IMethodInvoker invoker) {
        super(extCmd);
        this.invoker = invoker;
    }

    @Override
    protected void handleCommandRequest(IUserSession session, MessageLite message) throws Throwable {
        logger.debug("userId={}, account={}, cmd={} message={{}}", session.getUserId(), session.getAccount(), getCommand().getUniqueId(), message.toString());
        invoker.invoke(getCommand(), (MyUser) session.getUser(), message);
    }
}
