package com.ximuyi.game.core.scene.geography;

import java.util.function.BiConsumer;

public class PixRectangle {
    public final PixXYZ top_left;
    public final PixXYZ bottom_right;

    protected PixRectangle(PixXYZ pixXYZ0, PixXYZ pixXYZ1) {
        int minX = Math.min(pixXYZ0.x, pixXYZ1.x);
        int maxX = Math.max(pixXYZ0.x, pixXYZ1.x);
        int minY = Math.min(pixXYZ0.y, pixXYZ1.y);
        int maxY = Math.max(pixXYZ0.y, pixXYZ1.y);
        this.top_left = new PixXYZ(minX, minY);
        this.bottom_right = new PixXYZ(maxX, maxY);
    }

    public void forXY(BiConsumer<Integer, Integer> consumer){
        for (int x = top_left.x; x <= bottom_right.x; x++){
            for (int y = top_left.y; y <= bottom_right.y; y++){
                consumer.accept(x, y);
            }
        }
    }

    public boolean isCovered(PixXYZ pixXYZ){
        return top_left.x <= pixXYZ.x && bottom_right.x >= pixXYZ.x
                && top_left.y <= pixXYZ.y && bottom_right.y >= pixXYZ.y;
    }
}
