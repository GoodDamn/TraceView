package good.damn.marqueeview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import good.damn.marqueeview.interfaces.OnMarqueeFinishListener;
import good.damn.marqueeview.graphics.Line;
import good.damn.marqueeview.models.EntityConfig;

public class MarqueeView extends View implements View.OnTouchListener {

    private static final String TAG = "MarqueeView";

    private OnMarqueeFinishListener mOnMarqueeFinishListener;

    private boolean mIsFinished = false;

    private Line[] mLines;
    private EntityConfig[] mEntityConfigs;

    private Line mCurrentLineTouching;

    private void calculate() {

        if (getWidth() <= 10 && getHeight() <= 10) {
            return;
        }

        EntityConfig c;

        for (byte i = 0; i < mEntityConfigs.length; i++) {
            c = mEntityConfigs[i];
            mLines[i].onLayout(getWidth(), getHeight(),
                    c.fromX,c.fromY,c.toX,c.toY);
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
        if (mLines == null) {
            throw new IllegalStateException("ARRAY OF LINES IS NULL");
        }

        for (Line line: mLines) {
            line.reset();
        }
        mIsFinished = false;
        invalidate();
    }

    public void setVectorsSource(EntityConfig[] entityConfigs) {
        setOnTouchListener(null);

        mLines = new Line[entityConfigs.length];

        for (byte i = 0; i < mLines.length;i++) {
            mLines[i] = new Line();
            mLines[i].setColor(entityConfigs[i].color);
            mLines[i].setStrokeWidth(entityConfigs[i].strokeWidth);
        }

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

        if (mLines == null) {
            return;
        }

        for (Line mLine : mLines) {
            mLine.onDraw(canvas);
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

                mCurrentLineTouching = null;

                for (Line mLine : mLines) {
                    if (mLine.checkCollide(x,y)) {
                        mCurrentLineTouching = mLine;
                        break;
                    }
                }

                if (mCurrentLineTouching == null) {
                    return false;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                byte state = mCurrentLineTouching.onTouch(x,y);
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

                for (Line mLine : mLines) {
                    Log.d(TAG, "onTouch: MARQUEE_PROGRESS: " + mLine.getProgress());
                    if (mLine.getProgress() < 0.95)
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
