package com.ximuyi.akkagame.common.util;

import com.ximuyi.akkaserver.coder.ResponseCode;

public class ResultCode {
    public static ResultCode SUCCESS = new ResultCode(ResponseCode.SUCCESS);
    public static ResultCode SERVER_ERROR = new ResultCode(1);
    public static ResultCode PARAM_ERROR = new ResultCode(2);

    public static ResultCode SCENE_INVALID_GRID = new ResultCode(101);
    public static ResultCode SCENE_INVALID_POS = new ResultCode(102);
    public static ResultCode SCENE_NOT_IN_GRID = new ResultCode(103);
    public static ResultCode SCENE_NOT_IN_SAME = new ResultCode(104);
    public static ResultCode SCENE_MOVE_FAR = new ResultCode(105);

    private final short id;

    public ResultCode(int id) {
        this.id = (short) id;
    }

    public short getId() {
        return id;
    }

    public boolean isSuccess(){
        return id == ResponseCode.SUCCESS;
    }
}
