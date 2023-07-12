package good.damn.traceview.animators;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;

import good.damn.traceview.graphics.Entity;

public class ParallelAnimator extends EntityAnimator {

    public ParallelAnimator() {
        setDuration(2500);
        setInterpolator(new OvershootInterpolator());
    }

    @Override
    public void onUpdateDrawing(Canvas canvas) {
        for (Entity entity: mEntities) {
            entity.onAnimate(mProgress);
            entity.onDraw(canvas);
        }
    }
}