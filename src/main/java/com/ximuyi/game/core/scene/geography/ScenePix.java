package com.ximuyi.game.core.scene.geography;

public class ScenePix {
    private static final String OBSTACLE = " ABCDEFG";
    public static final char PATH = '.';
    public static final char WALK = '^';
    public static final char FLAT = '8';
    public static final char OBJECT = '*';
    public static final char TRAP = '.';

    public final int x;
    public final int y;
    public final long gridKey;
    private volatile char value;

    public ScenePix(int x, int y , char value) {
        this.x = x;
        this.y = y;
        this.gridKey = ((long) this.x << 32) | this.y;
        this.value = PATH == value ? OBSTACLE.charAt(0) : value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = OBSTACLE.charAt(value);
    }

    public void setValue(char value) {
        this.value = value;
    }

    public boolean isValid(int value){
        int index = OBSTACLE.indexOf(this.value);
        return index == -1 || index <= value;
    }

    public boolean canWalk(){
        return OBSTACLE.charAt(0) == value;
    }

    public char toChar(){
        return value;
    }

    public PixXYZ toPixXYZ(){
        return new PixXYZ(x, y);
    }
    public ScenePix toClone(){
        return new ScenePix(x, y, value);
    }
}
