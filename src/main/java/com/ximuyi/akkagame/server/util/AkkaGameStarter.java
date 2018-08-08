package com.ximuyi.akkagame.server.util;

import com.ximuyi.akkaserver.core.AkkaServer;

public class AkkaGameStarter {
    public static void main(String[] args) throws Throwable {
        AkkaServer.main(new String[]{ "1", "."});
    }
}
