package good.damn.traceview.animators;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

import good.damn.traceview.graphics.Entity;

public class SequenceAnimator extends EntityAnimator{

    private byte mCurrentEntityIndex;
    private Entity mCurrentEntity;

    public SequenceAnimator() {

        setDuration(1500);
        setInterpolator(new LinearInterpolator());

        addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                mCurrentEntityIndex++;
                if (mCurrentEntityIndex >= mEntities.length) {
                    return;
                }

                mCurrentEntity = mEntities[mCurrentEntityIndex];
                SequenceAnimator.super.start();
            }
            @Override public void onAnimationStart(@NonNull Animator animator) {}
            @Override public void onAnimationCancel(@NonNull Animator animator) {}
            @Override public void onAnimationRepeat(@NonNull Animator animator) {}
        });
    }

    @Override
    public void start() {
        mCurrentEntityIndex = 0;
        mCurrentEntity = mEntities[mCurrentEntityIndex];
        super.start();
    }

    @Override
    public void onUpdateDrawing(Canvas canvas) {
        for (Entity entity: mEntities) {
            entity.onDraw(canvas);
        }

        mCurrentEntity.onAnimate(mProgress);
        mCurrentEntity.onDraw(canvas);
    }
}
