package com.ximuyi.game.core.scene.geography;

public class SceneTrap extends PixRectangle{
    private final long uniqueId;
    private final int defineId;

    public SceneTrap(PixXYZ pixXYZ0, PixXYZ pixXYZ1, long uniqueId, int defineId) {
        super(pixXYZ0, pixXYZ1);
        this.uniqueId = uniqueId;
        this.defineId = defineId;
    }

    public long getUniqueId() {
        return uniqueId;
    }

    public int getDefineId() {
        return defineId;
    }
}
