package good.damn.marqueeview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import good.damn.marqueeview.graphics.Entity;
import good.damn.marqueeview.interfaces.OnMarqueeFinishListener;
import good.damn.marqueeview.graphics.Line;
import good.damn.marqueeview.models.EntityConfig;

public class MarqueeView extends View implements View.OnTouchListener {

    private static final String TAG = "MarqueeView";

    private OnMarqueeFinishListener mOnMarqueeFinishListener;

    private boolean mIsFinished = false;

    private EntityConfig[] mEntityConfigs;

    private Entity mCurrentEntityTouch;

    private void calculate() {

        if (getWidth() <= 10 && getHeight() <= 10) {
            return;
        }

        for (EntityConfig c : mEntityConfigs) {
            c.entity.onLayout(getWidth(), getHeight(),
                    c.fromX, c.fromY, c.toX, c.toY);
        }
    }

    private void init() {
        setOnTouchListener(this);
    }

    public MarqueeView(Context context) {
        super(context);
        init();
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void restart() {
        if (mEntityConfigs == null) {
            throw new IllegalStateException("ARRAY OF LINES IS NULL");
        }

        for (EntityConfig c: mEntityConfigs) {
            c.entity.reset();
        }
        mIsFinished = false;
        invalidate();
    }

    public void setVectorsSource(EntityConfig[] entityConfigs) {
        setOnTouchListener(null);
        mEntityConfigs = entityConfigs;

        calculate();

        setOnTouchListener(this);
    }

    public void setOnMarqueeFinishListener(OnMarqueeFinishListener  finishListener) {
        mOnMarqueeFinishListener = finishListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mEntityConfigs == null) {
            return;
        }

        for (EntityConfig c : mEntityConfigs) {
            c.entity.onDraw(canvas);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mEntityConfigs == null) {
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

                for (EntityConfig c : mEntityConfigs) {
                    if (c.entity.checkCollide(x,y)) {
                        mCurrentEntityTouch = c.entity;
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

                Log.d(TAG, "onTouch: ACTION_UP: IS_FINISHED: " + mIsFinished);

                if (mIsFinished) {
                    break;
                }

                // Check progress to finish

                for (EntityConfig c : mEntityConfigs) {
                    Log.d(TAG, "onTouch: MARQUEE_PROGRESS: " + c.entity.getProgress());
                    if (c.entity.getProgress() < 0.95)
                        return false;
                }

                mIsFinished = true;

                if (mOnMarqueeFinishListener != null) {
                    mOnMarqueeFinishListener.onFinish();
                }

                break;
        }

        return true;
    }
}
