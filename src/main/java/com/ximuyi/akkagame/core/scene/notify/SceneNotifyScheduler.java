package com.ximuyi.akkagame.core.scene.notify;

import com.ximuyi.akkagame.common.util.TaskScheduler;
import com.ximuyi.akkaserver.core.CoreAccessor;

public class SceneNotifyScheduler extends TaskScheduler<ISceneNotify> {

    private static final SceneNotifyScheduler instance = new SceneNotifyScheduler();

    public static SceneNotifyScheduler getInstance() {
        return instance;
    }

    public SceneNotifyScheduler() {
        super(CoreAccessor.getLocator().getScheduler());
    }
}
