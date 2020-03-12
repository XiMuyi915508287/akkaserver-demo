package com.ximuyi.game.core.sceneobject.ai;

import com.ximuyi.game.core.bt.BTContext;
import com.ximuyi.game.core.sceneobject.ISceneObject;

public class AIObjectContext implements BTContext {

    private final ISceneObject object;

    public AIObjectContext(ISceneObject object) {
        this.object = object;
    }

    public ISceneObject getObject() {
        return object;
    }
}
