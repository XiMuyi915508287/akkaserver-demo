package com.ximuyi.akkagame.common.extension;


import com.google.protobuf.MessageLite;
import com.ximuyi.akkagame.common.User;
import com.ximuyi.akkaserver.extension.ICommand;

import java.lang.reflect.InvocationTargetException;

@FunctionalInterface
public interface IMethodInvoker {
    void invoke(ICommand command, User user, MessageLite message) throws InvocationTargetException, IllegalAccessException;
}
