package com.ximuyi.game.core.sceneobject;

public enum ObjectType {
    Player(1, 999),
    ;

    public final int defineId;      //定义ID
    public final int orderTick;     //0 表示不执行定时调度， 小的优先级高

    ObjectType(int defineId, int orderTick) {
        this.defineId = defineId;
        this.orderTick = orderTick;
    }
}
