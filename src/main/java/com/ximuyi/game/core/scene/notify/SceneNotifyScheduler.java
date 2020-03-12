package com.ximuyi.game.core.scene.notify;

import com.ximuyi.game.common.util.MyScheduler;
import com.ximuyi.core.core.CoreAccessor;

public class SceneNotifyScheduler extends MyScheduler<ISceneNotify> {

    private static final SceneNotifyScheduler instance = new SceneNotifyScheduler();

    public static SceneNotifyScheduler getInstance() {
        return instance;
    }

    public SceneNotifyScheduler() {
        super(CoreAccessor.getInstance().getScheduler());
    }
}
