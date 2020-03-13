package com.ximuyi.game.core.scene.object.component;

import com.ximuyi.core.bt.BTNode;
import com.ximuyi.game.core.scene.object.ai.AIObjectContext;
import com.ximuyi.game.core.scene.object.ai.AIObjectManager;

public class ObjectCompAI extends ObjectComponent implements IObjectCompAI {

    private final int btId;


    public ObjectCompAI(int btId) {
        super(ComponentType.AI);
        this.btId = btId;
    }

    @Override
    public void onScheduled() {
        BTNode<AIObjectContext> btNode = AIObjectManager.getInstance().getAI(btId);
        btNode.run(new AIObjectContext(getObject()));
    }
}
