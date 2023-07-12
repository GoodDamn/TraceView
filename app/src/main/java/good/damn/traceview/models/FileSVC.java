package good.damn.traceview.models;

import androidx.annotation.Nullable;

import good.damn.traceview.animators.EntityAnimator;
import good.damn.traceview.graphics.Entity;

public class FileSVC {

    public static final byte TYPE_INTERACTION = 0;
    public static final byte TYPE_ANIMATION = 1;

    public static final byte ANIMATOR_PARALLEL = 0;
    public static final byte ANIMATOR_SEQUENCE = 1;


    public Entity[] entities;
    public boolean isInteractive;

    @Nullable
    public EntityAnimator animator;
}
