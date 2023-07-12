package good.damn.traceview.animators;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.util.Log;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;

import java.security.cert.CertificateNotYetValidException;

import good.damn.traceview.graphics.Entity;
import good.damn.traceview.views.TraceView;

public abstract class EntityAnimator {

    private static final String TAG = "EntityAnimator";

    private final ValueAnimator mAnimator;
    private TraceView mTraceView;

    protected Entity[] mEntities;
    protected float mProgress;

    public EntityAnimator() {
        mProgress = 0;

        mAnimator = new ValueAnimator();
        mAnimator.setIntValues(0,1);

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                mProgress = valueAnimator.getAnimatedFraction();
                mTraceView.invalidate();
            }
        });
    }

    public void setTraceView(TraceView traceView) {
        mTraceView = traceView;
    }

    public void setEntities(Entity[] entities) {
        mEntities = entities;
    }

    protected void addListener(Animator.AnimatorListener listener) {
        mAnimator.addListener(listener);
    }

    public void setDuration(int duration) {
        mAnimator.setDuration(duration);
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        mAnimator.setInterpolator(interpolator);
    }

    public void start() {
        if (mEntities == null) {
            throw new IllegalArgumentException("Entity[] == NULL. YOU NEED TO PASS A REFERENCE to setEntities(Entity[]);");
        }

        if (mTraceView == null) {
            throw new IllegalArgumentException("YOU NEED TO PASS A REFERENCE to setTraceView(TraceView);");
        }
        mAnimator.start();
    }

    public abstract void onUpdateDrawing(Canvas canvas);

}
