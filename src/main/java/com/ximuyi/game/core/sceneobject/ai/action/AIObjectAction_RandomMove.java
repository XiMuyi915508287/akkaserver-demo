package com.ximuyi.game.core.sceneobject.ai.action;

import com.ximuyi.core.coder.ResultCode;
import com.ximuyi.game.common.util.Args;
import com.ximuyi.game.core.bt.BTAction;
import com.ximuyi.game.core.scene.IScene;
import com.ximuyi.game.core.scene.PosXYZ;
import com.ximuyi.game.core.scene.geography.PixXYZ;
import com.ximuyi.game.core.sceneobject.ai.AIObjectContext;

public class AIObjectAction_RandomMove extends BTAction<AIObjectContext> {

    @Override
    protected boolean action(AIObjectContext context) {
        Args.Two<IScene, PosXYZ> entry = context.getObject().getLocation();
        if (entry.eitherNull()){
            return false;
        }
        IScene scene = entry.arg0;
        PixXYZ end = scene.getTerrain().availablePix(scene.getTerrain().transform(entry.arg1));
        ResultCode resultCode = scene.move(context.getObject(), scene.getTerrain().transform(end));
        return resultCode.isSuccess();
    }
}
