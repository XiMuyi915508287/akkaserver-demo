package com.ximuyi.akkagame.core.sceneobject.ai.action;

import com.ximuyi.akkagame.common.util.PackageClassFactory;
import com.ximuyi.akkagame.core.bt.BTNode;
import com.ximuyi.akkagame.core.sceneobject.ai.AIObjectContext;

public class AIObjectFactory {

    private static final PackageClassFactory<BTNode<AIObjectContext>> classFactory = new PackageClassFactory<>(
            AIObjectFactory.class, "AIObjectAction_");

    public static BTNode<AIObjectContext> getAIAction(String name) {
        char c = name.charAt(0);
        if (c >= 'a' && c <= 'z') {
            c -= 32;
            name = c + name.substring(1);
        }
        return classFactory.create(name);
    }
}
