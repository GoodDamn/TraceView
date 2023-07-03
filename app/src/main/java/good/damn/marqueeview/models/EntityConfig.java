package good.damn.marqueeview.models;

import good.damn.marqueeview.graphics.Entity;
import good.damn.marqueeview.graphics.editor.EntityEditor;

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
