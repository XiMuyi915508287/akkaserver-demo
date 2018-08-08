package com.ximuyi.akkagame.core.sceneobject.ai;

import com.ximuyi.akkagame.core.bt.BTContext;
import com.ximuyi.akkagame.core.sceneobject.ISceneObject;

public class AIObjectContext implements BTContext {

    private final ISceneObject object;

    public AIObjectContext(ISceneObject object) {
        this.object = object;
    }

    public ISceneObject getObject() {
        return object;
    }
}
