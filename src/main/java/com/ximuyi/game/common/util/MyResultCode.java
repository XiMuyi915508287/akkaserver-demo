package com.ximuyi.game.common.util;

import com.ximuyi.core.coder.ResultCode;

public class MyResultCode {

    public static ResultCode SUCCESS = ResultCode.SUCCESS;
    public static ResultCode SERVER_ERROR = ResultCode.customize((short)1, "");
    public static ResultCode PARAM_ERROR = ResultCode.customize((short)2, "");

    public static ResultCode SCENE_INVALID_GRID = ResultCode.customize((short)101, "");
    public static ResultCode SCENE_INVALID_POS = ResultCode.customize((short)102, "");
    public static ResultCode SCENE_NOT_IN_GRID = ResultCode.customize((short)103, "");
    public static ResultCode SCENE_NOT_IN_SAME = ResultCode.customize((short)104, "");
    public static ResultCode SCENE_MOVE_FAR = ResultCode.customize((short)105, "");
}
