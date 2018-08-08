package com.ximuyi.akkagame.core.sceneobject.component;

import com.ximuyi.akkagame.core.bt.BTNode;
import com.ximuyi.akkagame.core.sceneobject.ai.AIObjectContext;
import com.ximuyi.akkagame.core.sceneobject.ai.AIObjectManager;

public class ObjetCompAI extends ObjetComponent implements IObjetCompAI{

    private final int btId;


    public ObjetCompAI(int btId) {
        super(ComponentType.AI);
        this.btId = btId;
    }

    @Override
    public void onTick() {
        BTNode<AIObjectContext> btNode = AIObjectManager.getInstance().getAI(btId);
        btNode.run(new AIObjectContext(getObject()));
    }
}
