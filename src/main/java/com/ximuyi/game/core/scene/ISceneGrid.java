package com.ximuyi.game.core.scene;

import com.ximuyi.game.core.scene.object.ISceneObject;
import com.ximuyi.game.core.scene.object.ObjectType;

import java.util.Collection;

public interface ISceneGrid{
    int getGridX();
    int getGridY();
    long getUniqueId();
    boolean add(ISceneObject object);
    boolean remove(ISceneObject object);
    <T extends ISceneObject> Collection<T> objects(ObjectType type);
    int objectNum();
}
