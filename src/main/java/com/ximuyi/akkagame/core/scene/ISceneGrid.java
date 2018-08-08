package com.ximuyi.akkagame.core.scene;

import com.ximuyi.akkagame.core.sceneobject.ISceneObject;
import com.ximuyi.akkagame.core.sceneobject.ObjectType;

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
