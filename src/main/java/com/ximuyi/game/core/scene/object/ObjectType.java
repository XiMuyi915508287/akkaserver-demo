package com.ximuyi.game.core.scene.object;

public enum ObjectType {
    Player(1, 999),
    ;

    public final int defineId;      //定义ID
    public final int priority;     //0 表示不执行定时调度， 小的优先级高

    ObjectType(int defineId, int priority) {
        this.defineId = defineId;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isScheduled(){
        return getPriority() > 0;
    }
}
