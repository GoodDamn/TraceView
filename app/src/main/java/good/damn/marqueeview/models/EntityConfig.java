package good.damn.marqueeview.models;

import good.damn.marqueeview.graphics.editor.EntityEditor;

public class EntityConfig {
    public float fromX;
    public float fromY;

    public float toX;
    public float toY;

    public int color = 0xffff0000;
    public byte strokeWidth = 15;

    public EntityEditor entity;

    public EntityConfig() {}

    public EntityConfig(float fx, float fy, float tx, float ty) {
        fromX = fx;
        fromY = fy;
        toX = tx;
        toY = ty;
    }
}
