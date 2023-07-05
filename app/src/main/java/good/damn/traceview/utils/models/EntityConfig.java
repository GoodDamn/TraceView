package good.damn.traceview.utils.models;

import good.damn.traceview.graphics.Entity;

public class EntityConfig {
    public float fromX;
    public float fromY;

    public float toX;
    public float toY;

    public Entity entity;

    public EntityConfig() {}

    public EntityConfig(float fx, float fy, float tx, float ty) {
        fromX = fx;
        fromY = fy;
        toX = tx;
        toY = ty;
    }
}
