package good.damn.traceview.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

public abstract class Entity {

    private static final String TAG = "Entity";

    public static final byte DRAW_INVALIDATE_WITH_FALSE = 0;
    public static final byte DRAW_FALSE = 1;
    public static final byte DRAW_TRUE = 2;

    private float mStartNormalX;
    private float mStartNormalY;
    private float mEndNormalX;
    private float mEndNormalY;

    protected final Random mRandom = new Random();

    protected final boolean RELEASE_MODE = true;

    protected final Paint mPaintForeground = new Paint();
    protected final Paint mPaintBackground = new Paint();
    protected final Paint mPaintDebug = new Paint();

    protected final int mStickBound = 25;

    protected float mTraceStartX = 0;
    protected float mTraceStartY = 0;
    protected float mTraceEndX = 1;
    protected float mTraceEndY = 0;

    protected float mStickX = 0;
    protected float mStickY = 0;

    protected float mProgress = 0f;

    protected int mWidth = 1;
    protected int mHeight = 1;

    protected boolean mHasPivot = false;

    protected boolean boxBound(float x, float y, float curX, float curY) {
        return curX-mStickBound < x && x < curX+mStickBound &&
                curY-mStickBound < y && y < curY+mStickBound;
    }

    public Entity() {
        mPaintForeground.setColor(0xff00ff59);
        mPaintForeground.setStrokeWidth(10);
        mPaintForeground.setStrokeCap(Paint.Cap.ROUND);

        mPaintDebug.setColor(0xffffff00);
        mPaintDebug.setStyle(Paint.Style.STROKE);
        mPaintDebug.setTextSize(15.0f);

        mPaintBackground.setColor(0x55ffffff);
        mPaintBackground.setStrokeWidth(10);
        mPaintBackground.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setColor(int color) {
        mPaintForeground.setColor(color);
    }

    public int getColor() {
        return mPaintForeground.getColor();
    }

    public float getProgress() {
        return Math.abs(mProgress);
    }

    public void setStrokeWidth(byte width) {
        mPaintForeground.setStrokeWidth(width);
        mPaintBackground.setStrokeWidth(width);
    }

    public void setStartNormalPoint(float startX, float startY) {
        mStartNormalX = startX;
        mStartNormalY = startY;
    }

    public void setEndNormalPoint(float endX, float endY) {
        mEndNormalX = endX;
        mEndNormalY = endY;
    }

    public void reset() {
        mStickX = mTraceStartX;
        mStickY = mTraceStartY;

        mHasPivot = false;

        mProgress = 0;
    }

    public void onLayout(int width, int height) {
        Log.d(TAG, "onLayout: Line::onLayout();");
        mWidth = width;
        mHeight = height;

        mTraceStartX = width * mStartNormalX;
        mTraceStartY = height * mStartNormalY;

        mTraceEndX = width * mEndNormalX;
        mTraceEndY = height * mEndNormalY;

        mStickX = mTraceStartX;
        mStickY = mTraceStartY;
    }


    public void onDraw(Canvas canvas) {

        float x = mStickX - mStickBound;
        float y = mStickY - mStickBound;
        canvas.drawRect(x, y,
                mStickX + mStickBound,
                mStickY + mStickBound,
                mPaintDebug);

        canvas.drawText("Y: " + y, x, y - mPaintDebug.getTextSize(), mPaintDebug);
        canvas.drawText("X: " + x, x, y - mPaintDebug.getTextSize() * 2, mPaintDebug);
    }

    public final byte onTouch(float x, float y) {
        Log.d(TAG, "onTouch: X: " + x + " " + y);
        if (checkDeltaInBounds(x,y)) {
            onPlace(x,y);
            return DRAW_INVALIDATE_WITH_FALSE;
        }

        return DRAW_FALSE;
    }

    public boolean hasPivot() {
        return mHasPivot;
    }

    public void onSetupPivotPoint(float x, float y){}

    public void onTouchUp(){}

    public abstract boolean checkCollide(float x, float y);

    abstract void onPlace(float x, float y);
    abstract boolean checkDeltaInBounds(float x, float y);
}
