package com.ximuyi.akkagame.common.extension;

import com.google.common.collect.ImmutableList;
import com.google.protobuf.MessageLite;
import com.ximuyi.akkagame.server.coder.MyMessageCoder;
import com.ximuyi.akkaserver.config.ConfigWrapper;
import com.ximuyi.akkaserver.core.CoreAccessor;
import com.ximuyi.akkaserver.extension.request.IRequestHandler;
import com.ximuyi.akkaserver.extension.request.RequestHandlerUtil;
import com.ximuyi.akkaserver.utils.YamlUtils;

import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ReqExtensionManager {

    private static ImmutableList<RequestExtension> extensions;

    public static void init(ConfigWrapper wrapper) throws FileNotFoundException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ImmutableList.Builder<RequestExtension> builder = ImmutableList.builder();
        Map<Integer,String> configs = YamlUtils.loadConfig(wrapper.getFilePath("extensions.yaml"), Map.class);
        if (configs == null || configs.isEmpty()) {
            return;
        }
        for (Map.Entry<Integer, String> entry: configs.entrySet()) {
            short id = entry.getKey().shortValue();
            Class<?> cls = Class.forName(entry.getValue());
            //Short.class 跟 short.class 不匹配的
            Constructor<?> constructor = cls.getConstructor(short.class);
            RequestExtension extension = (RequestExtension)constructor.newInstance(id);
            builder.add(extension);
        }
        extensions = builder.build();
        extensions.forEach( extension -> extension.init());
    }

    public static void register(IRequestHandler<? extends MessageLite> handler, MessageLite message){
        RequestHandlerUtil.reload(handler);
        CoreAccessor.getLocator().getManager(MyMessageCoder.class).register(handler.getCmd(), message);
    }
}
