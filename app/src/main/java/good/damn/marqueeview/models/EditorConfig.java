package good.damn.marqueeview.models;

import good.damn.marqueeview.graphics.editor.EntityEditor;

public class EditorConfig extends EntityConfig {
    public EntityEditor entityEditor;

    public EditorConfig(float fx, float fy, float tx, float ty) {
        super(fx, fy, tx, ty);
    }
}
