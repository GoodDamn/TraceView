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

        addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {
                for (Entity entity: mEntities) {
                    entity.onPrepareAnimation();
                }
            }
            @Override public void onAnimationEnd(@NonNull Animator animator) {}
            @Override public void onAnimationCancel(@NonNull Animator animator) {}
            @Override public void onAnimationRepeat(@NonNull Animator animator) {}
        });
    }

    @Override
    public void onUpdateDrawing(Canvas canvas) {
        for (Entity entity: mEntities) {
            entity.onAnimate(mProgress);
            entity.onDraw(canvas);
        }
    }
}