package com.ximuyi.game.common.extension;


import java.lang.reflect.InvocationTargetException;

import com.google.protobuf.MessageLite;
import com.ximuyi.game.common.MyUser;
import com.ximuyi.core.command.ICommand;

@FunctionalInterface
public interface IMethodInvoker {
    void invoke(ICommand command, MyUser user, MessageLite message) throws InvocationTargetException, IllegalAccessException;
}
