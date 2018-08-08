package com.ximuyi.akkagame.core.sceneobject.component;

import com.ximuyi.akkagame.core.bt.BTNode;
import com.ximuyi.akkagame.core.sceneobject.ai.AIObjectContext;
import com.ximuyi.akkagame.core.sceneobject.ai.AIObjectManager;

public class ObjetCompMove extends ObjetComponent implements IObjetCompAI{

    public ObjetCompMove() {
        super(ComponentType.AI);
    }

    @Override
    public void onTick() {
        BTNode<AIObjectContext> btNode = AIObjectManager.getInstance().getAI(1);
        btNode.run(new AIObjectContext(getObject()));
    }
}
