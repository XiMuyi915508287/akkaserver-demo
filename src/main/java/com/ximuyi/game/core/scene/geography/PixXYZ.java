package com.ximuyi.game.core.scene.geography;

public class PixXYZ {
    public final int x;
    public final int y;
    public final int z;
    public final long gridKey;

    public PixXYZ(int x, int y) {
        this(x, y, 0);
    }

    public PixXYZ(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.gridKey = ((long) this.x << 32) | this.y;
    }

    public int distance(PixXYZ pixXYZ){
        double x = Math.pow(pixXYZ.x - this.x, 2);
        double y = Math.pow(pixXYZ.y - this.y, 2);
        return (int)Math.floor(Math.sqrt( x + y));
    }
}
