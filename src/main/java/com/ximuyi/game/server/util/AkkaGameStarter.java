package com.ximuyi.game.server.util;

import com.ximuyi.core.AkkaServer;
import com.ximuyi.core.env.EnvKeys;

public class AkkaGameStarter {

    /**
     * 启动参数：
     * -Dlog4j.configurationFile=config/log4j2.xml
     * @param args
     * @throws Throwable
     */
    public static void main(String[] args) throws Throwable {
        System.setProperty(EnvKeys.SERVER_HOME, ".");
        System.setProperty(EnvKeys.SERVER_HOME, ".");
        AkkaServer.main(new String[]{ "1"});
    }
}
