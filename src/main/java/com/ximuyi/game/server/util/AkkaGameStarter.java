package com.ximuyi.game.server.util;

import com.ximuyi.core.core.AkkaServer;
import com.ximuyi.core.env.EnvKeys;

public class AkkaGameStarter {

    public static void main(String[] args) throws Throwable {
        System.setProperty(EnvKeys.SERVER_HOME, ".");
        AkkaServer.main(new String[]{ "1"});
    }
}
