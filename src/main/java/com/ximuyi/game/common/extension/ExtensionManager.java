package com.ximuyi.game.common.extension;

import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.protobuf.MessageLite;
import com.ximuyi.game.server.coder.MyMessageCoder;
import com.ximuyi.core.config.Configs;
import com.ximuyi.core.core.CoreAccessor;
import com.ximuyi.core.command.handler.ICommandHandler;
import com.ximuyi.core.command.handler.CommandHandlerUtil;
import com.ximuyi.core.utils.YamlUtils;

public class ExtensionManager {

    private static ImmutableList<Extension> extensions;

    public static void init() throws FileNotFoundException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ImmutableList.Builder<Extension> builder = ImmutableList.builder();
        String filePath = Configs.getInstance().getFilePath("config/extensions.yaml");
        Map<Integer,String> configs = YamlUtils.loadConfig(filePath, Map.class);
        if (configs == null || configs.isEmpty()) {
            return;
        }
        for (Map.Entry<Integer, String> entry: configs.entrySet()) {
            short id = entry.getKey().shortValue();
            Class<?> cls = Class.forName(entry.getValue());
            //Short.class 跟 short.class 不匹配的
            Constructor<?> constructor = cls.getConstructor(short.class);
            Extension extension = (Extension)constructor.newInstance(id);
            builder.add(extension);
        }
        extensions = builder.build();
        extensions.forEach( extension -> extension.init());
    }

    public static void register(ICommandHandler<? extends MessageLite> handler, MessageLite message){
        CommandHandlerUtil.reload(handler);
        CoreAccessor.getInstance().getManager(MyMessageCoder.class).register(handler.getCommand(), message);
    }
}
