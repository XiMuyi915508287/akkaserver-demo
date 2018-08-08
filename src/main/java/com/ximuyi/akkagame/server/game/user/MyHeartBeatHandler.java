package com.ximuyi.akkagame.server.game.user;

import com.ximuyi.akkagame.common.util.DateUtil;
import com.ximuyi.akkagame.server.proto.ProtoUser;
import com.ximuyi.akkaserver.extension.request.HeartBeatHanlder;

public class MyHeartBeatHandler extends HeartBeatHanlder<ProtoUser.HeartBeatRequest, ProtoUser.HeartBeatResponse> {

    @Override
    protected ProtoUser.HeartBeatResponse getMessage(ProtoUser.HeartBeatRequest messsage) {
        return ProtoUser.HeartBeatResponse.newBuilder().setCurrentTime(DateUtil.getCurrentSecond()).build();
    }
}
