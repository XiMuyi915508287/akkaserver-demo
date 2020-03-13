package com.ximuyi.game.core.scene.object.ai;

import com.ximuyi.core.bt.BTContext;
import com.ximuyi.game.core.scene.object.ISceneObject;

public class AIObjectContext implements BTContext {

    private final ISceneObject object;

    public AIObjectContext(ISceneObject object) {
        this.object = object;
    }

    public ISceneObject getObject() {
        return object;
    }
}
