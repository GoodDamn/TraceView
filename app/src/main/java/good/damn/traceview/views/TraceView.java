package good.damn.traceview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import good.damn.traceview.animators.VectorAnimator;
import good.damn.traceview.graphics.Entity;
import good.damn.traceview.graphics.Line;
import good.damn.traceview.interfaces.OnTraceFinishListener;

public class TraceView extends View implements View.OnTouchListener {

    private static final String TAG = "MarqueeView";

    protected final float COMPLETE_PROGRESS_TRIGGER = 0.95f;

    private OnTraceFinishListener mOnTraceFinishListener;

    private boolean mIsFinished = false;

    private Entity[] mEntities;

    private Entity mCurrentEntityTouch;

    private void calculate() {

        if (getWidth() <= 10 && getHeight() <= 10) {
            return;
        }

        for (Entity e : mEntities) {
            e.onLayout(getWidth(), getHeight());
        }
    }

    private void init() {
        setOnTouchListener(this);
    }

    public TraceView(Context context) {
        super(context);
        init();
    }

    public TraceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TraceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void restart() {
        if (mEntities == null) {
            throw new IllegalStateException("ARRAY OF LINES IS NULL");
        }

        for (Entity e: mEntities) {
            e.reset();
        }
        mIsFinished = false;
        invalidate();
    }

    public void setVectorsSource(Entity[] entities) {
        setOnTouchListener(null);
        mEntities = entities;

        calculate();

        setOnTouchListener(this);
        invalidate();
    }

    public void setOnTraceFinishListener(OnTraceFinishListener finishListener) {
        mOnTraceFinishListener = finishListener;
    }

    public void animate(VectorAnimator vectorAnimator) {
        vectorAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mEntities == null) {
            return;
        }

        for (Entity c : mEntities) {
            c.onDraw(canvas);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mEntities == null) {
            return;
        }

        calculate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        Log.d(TAG, "onTouch: X: " + x + " Y: " + y);

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mCurrentEntityTouch = null;

                for (Entity entity : mEntities) {

                    if (!entity.hasPivot()) {
                        entity.onSetupPivotPoint(x,y);
                        invalidate();
                    }

                    if (entity.checkCollide(x,y)) {
                        mCurrentEntityTouch = entity;
                        break;
                    }
                }

                if (mCurrentEntityTouch == null) {
                    return false;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                byte state = mCurrentEntityTouch.onTouch(x,y);
                if (state == Line.DRAW_INVALIDATE_WITH_FALSE) {
                    invalidate();
                    return false;
                }

                if (state == Line.DRAW_FALSE) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:

                mCurrentEntityTouch.onTouchUp();

                Log.d(TAG, "onTouch: ACTION_UP: IS_FINISHED: " + mIsFinished);

                if (mIsFinished) {
                    break;
                }

                // Check progress to finish

                for (Entity entity : mEntities) {
                    Log.d(TAG, "onTouch: MARQUEE_PROGRESS: " + entity.getProgress());
                    if (entity.getProgress() < COMPLETE_PROGRESS_TRIGGER)
                        return false;
                }

                mIsFinished = true;

                if (mOnTraceFinishListener != null) {
                    mOnTraceFinishListener.onFinish();
                }

                break;
        }

        return true;
    }
}
