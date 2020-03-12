package com.ximuyi.game.core.bt;

import com.ximuyi.game.common.util.RandomUtil;

public class BTNodeRandom<T extends BTContext> extends BTNodeBond<T> {

    @Override
    public boolean enableRun(T context) {
        BTNode<T> btNode = RandomUtil.nextItem(getChildren(), child-> child.getWeight());
        return btNode.run(context);
    }
}
