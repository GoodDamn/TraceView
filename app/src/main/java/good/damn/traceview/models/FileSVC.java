package good.damn.traceview.models;

import androidx.annotation.Nullable;

import good.damn.traceview.animators.EntityAnimator;
import good.damn.traceview.graphics.Entity;

public class FileSVC {

    public Entity[] entities;
    public boolean isInteractive;

    @Nullable
    public EntityAnimator animator;
}
