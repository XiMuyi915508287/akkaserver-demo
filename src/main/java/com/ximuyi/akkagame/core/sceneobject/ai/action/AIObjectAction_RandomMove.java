package com.ximuyi.akkagame.core.sceneobject.ai.action;

import com.ximuyi.akkagame.common.util.DoubleEntry;
import com.ximuyi.akkagame.common.util.ResultCode;
import com.ximuyi.akkagame.core.bt.BTAction;
import com.ximuyi.akkagame.core.scene.IScene;
import com.ximuyi.akkagame.core.scene.PosXYZ;
import com.ximuyi.akkagame.core.scene.geography.PixXYZ;
import com.ximuyi.akkagame.core.sceneobject.ai.AIObjectContext;

public class AIObjectAction_RandomMove extends BTAction<AIObjectContext> {

    @Override
    protected boolean action(AIObjectContext context) {
        DoubleEntry<IScene, PosXYZ>  entry = context.getObject().getLocation();
        if (entry.eitherNull()){
            return false;
        }
        IScene scene = entry.first;
        PixXYZ end = scene.getTerrain().availablePix(scene.getTerrain().transform(entry.second));
        ResultCode resultCode = scene.move(context.getObject(), scene.getTerrain().transform(end));
        return resultCode.isSuccess();
    }
}
