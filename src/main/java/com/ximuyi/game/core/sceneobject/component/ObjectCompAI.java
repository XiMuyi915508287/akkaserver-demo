package com.ximuyi.game.core.sceneobject.component;

import com.ximuyi.game.core.bt.BTNode;
import com.ximuyi.game.core.sceneobject.ai.AIObjectContext;
import com.ximuyi.game.core.sceneobject.ai.AIObjectManager;

public class ObjectCompAI extends ObjetComponent implements IObjetCompAI{

    private final int btId;


    public ObjectCompAI(int btId) {
        super(ComponentType.AI);
        this.btId = btId;
    }

    @Override
    public void onTick() {
        BTNode<AIObjectContext> btNode = AIObjectManager.getInstance().getAI(btId);
        btNode.run(new AIObjectContext(getObject()));
    }
}
