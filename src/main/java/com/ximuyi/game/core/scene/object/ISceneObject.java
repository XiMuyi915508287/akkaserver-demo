package com.ximuyi.game.core.scene.object;

import com.ximuyi.game.common.util.Args;
import com.ximuyi.game.core.scene.IScene;
import com.ximuyi.game.core.scene.ISceneScheduler;
import com.ximuyi.game.core.scene.PosXYZ;
import com.ximuyi.game.core.scene.object.component.IObjectComponent;
import com.ximuyi.game.core.scene.object.component.ComponentType;

public interface ISceneObject extends ISceneScheduler {
    long getUniqueId();
    PosXYZ getPos();
    IScene getScene();
    void setPos(IScene scene, PosXYZ posXYZ);
    int trapWeight();
    ObjectType getType();
    Args.Two<IScene, PosXYZ> getLocation();
    <T extends IObjectComponent> T getComponent(ComponentType type);
    void addComponent(IObjectComponent component);
    <T extends IObjectComponent> T removeComponent(ComponentType type);
}
