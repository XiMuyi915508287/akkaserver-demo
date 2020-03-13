package com.ximuyi.game.core.scene;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.ximuyi.core.coder.ResultCode;
import com.ximuyi.game.core.scene.geography.ISceneTerrain;
import com.ximuyi.game.core.scene.object.ISceneObject;
import com.ximuyi.game.core.scene.object.ObjectType;

public interface IScene extends ISceneScheduler {

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
