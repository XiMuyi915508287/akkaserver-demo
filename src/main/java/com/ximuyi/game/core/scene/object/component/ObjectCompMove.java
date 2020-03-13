package com.ximuyi.game.core.scene.object.component;

import com.ximuyi.core.bt.BTNode;
import com.ximuyi.game.core.scene.object.ai.AIObjectContext;
import com.ximuyi.game.core.scene.object.ai.AIObjectManager;

public class ObjectCompMove extends ObjectComponent implements IObjectCompAI {

    public ObjectCompMove() {
        super(ComponentType.AI);
    }

    @Override
    public void onScheduled() {
        BTNode<AIObjectContext> btNode = AIObjectManager.getInstance().getAI(2000);
        btNode.run(new AIObjectContext(getObject()));
    }
}
