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

    protected final Random mRandom = new Random();

    protected final boolean RELEASE_MODE = true;

    protected final Paint mPaintForeground = new Paint();
    protected final Paint mPaintBackground = new Paint();
    protected final Paint mPaintDebug = new Paint();

    protected final int mStickBound = 50;

    protected float mMarStartX = 0;
    protected float mMarStartY = 0;
    protected float mMarEndX = 1;
    protected float mMarEndY = 0;

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

    public void reset() {
        mStickX = mMarStartX;
        mStickY = mMarStartY;

        mHasPivot = false;

        mProgress = 0;
    }

    public void onLayout(int width, int height,
                           float startX, float startY,
                           float endX, float endY) {
        Log.d(TAG, "onLayout: Line::onLayout();");
        mWidth = width;
        mHeight = height;

        mMarStartX = width * startX;
        mMarStartY = height * startY;

        mMarEndX = width * endX;
        mMarEndY = height * endY;

        mStickX = mMarStartX;
        mStickY = mMarStartY;
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
