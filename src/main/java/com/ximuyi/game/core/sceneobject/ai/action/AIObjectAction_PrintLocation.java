package com.ximuyi.game.core.sceneobject.ai.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ximuyi.core.coder.ResultCode;
import com.ximuyi.game.common.util.Args;
import com.ximuyi.game.core.bt.BTAction;
import com.ximuyi.game.core.scene.IScene;
import com.ximuyi.game.core.scene.PathFinder;
import com.ximuyi.game.core.scene.PosXYZ;
import com.ximuyi.game.core.scene.geography.PixXYZ;
import com.ximuyi.game.core.scene.geography.SceneView;
import com.ximuyi.game.core.sceneobject.ISceneObject;
import com.ximuyi.game.core.sceneobject.ObjectType;
import com.ximuyi.game.core.sceneobject.ai.AIObjectContext;

public class AIObjectAction_PrintLocation extends BTAction<AIObjectContext> {

    @Override
    protected boolean action(AIObjectContext context) {
        Args.Two<IScene, PosXYZ>  entry = context.getObject().getLocation();
        if (entry.eitherNull()){
            return false;
        }
        IScene scene = entry.arg0;
        PathFinder finder = new PathFinder(scene);
        PixXYZ end = scene.getTerrain().availablePix(scene.getTerrain().transform(entry.arg1));
        ResultCode resultCode = scene.move(context.getObject(), scene.getTerrain().transform(end));
        if (resultCode.isSuccess()){
            PixXYZ begin = scene.getTerrain().transform(entry.arg1);
            long current = System.currentTimeMillis();
            List<PixXYZ> pixXYZList = finder.searchPath(context.getObject(), begin, end);
            long cost = System.currentTimeMillis() - current;
            boolean failure = pixXYZList == null;
            if (failure){
                pixXYZList = new ArrayList<>();
            }
            if (context.getObject().getUniqueId() == 1){
                SceneView sceneView = scene.getTerrain().getView();
                Collection<ISceneObject> objects = scene.objects(ObjectType.Player);
                for (ISceneObject object : objects) {
                    if (object.getUniqueId() == 1){
                        continue;
                    }
                    sceneView.drawObject(scene.getTerrain().transform(object.getPos()));
                }
                sceneView.drawPath(begin, end, pixXYZList);
//                printLine();
//                System.out.println(StringUtil.join(Arrays.asList(sceneView.getMap()), "\n"));
//                String descibe = String.format("当前路径[%d] 当前耗时：%d (ms)\n\n\n\n", pixXYZList.size(), cost);
//                if (failure){
//                    descibe = "失败失败失败---" + descibe;
//                }
//                printLine();
//                System.out.println(descibe);
            }
        }
        return resultCode.isSuccess();
    }

    private void printLine(){
        System.out.println("------------------------------------------------------------------------------------------" +
                                   "----------------------------------------------------------------------------------" +
                                   "----------------------------------------------------------------------------------" +
                                   "----------------------------------------------------------------------------------" +
                                   "----------------------------------------------------------------------------------" +
                                   "----------------------------------------------------------------------------------" +
                                   "----------------------------------------------------------------------------------" +
                                   "----------------------------------------------------------------------------------" +
                                   "----------------------------------------------------------------------------------" +
                                   "----------------------------------------------------------------------------------" +
                                   "----------------------------------------------------------------------------------" +
                                   "----------------------------------------------------------------------------------" +
                                   "----------------------------------------------------------------------------------" +
                                   "----------------------------------------------------------------------------------");
    }
}
