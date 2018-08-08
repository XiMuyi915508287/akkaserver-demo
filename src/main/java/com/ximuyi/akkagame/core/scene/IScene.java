package com.ximuyi.akkagame.core.scene;

import com.ximuyi.akkagame.common.util.ResultCode;
import com.ximuyi.akkagame.core.scene.geography.ISceneTerrain;
import com.ximuyi.akkagame.core.sceneobject.ISceneObject;
import com.ximuyi.akkagame.core.sceneobject.ObjectType;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface IScene extends ISceneSchedule {

    long getUniqueId();

    int getDefineId();

    SceneOwn getSceneOwn();
    
    int objectNum();

    <T extends ISceneObject> Collection<T> objects(ObjectType type);

    ResultCode join(ISceneObject object, PosXYZ posXYZ);

    ResultCode leave(ISceneObject object);

    ResultCode move(ISceneObject object, PosXYZ posXYZ);

    void forGrid(Consumer<ISceneGrid> consumer, Predicate<ISceneGrid> predicate);

    ISceneTerrain getTerrain();
}
