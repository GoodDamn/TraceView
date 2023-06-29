package good.damn.marqueeview.models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;

public class Line {

    private static final String TAG = "Line";

    public static final byte DRAW_INVALIDATE_WITH_FALSE = 0;
    public static final byte DRAW_FALSE = 1;
    public static final byte DRAW_TRUE = 2;

    private static final boolean DEBUG_MODE = false;

    private final Random mRandom = new Random();

    private final Paint mPaintForeground = new Paint();
    private final Paint mPaintBackground = new Paint();
    private final Paint mPaintDebug = new Paint();

    private final int mStickBound = 50;

    private float mMarStartX = 0;
    private float mMarStartY = 0;
    private float mMarEndX = 1;
    private float mMarEndY = 0;

    private float mStickX = 0;
    private float mStickY = 0;

    private int mWidth = 1;
    private int mHeight = 1;

    public Line() {
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

    public void onDraw(Canvas canvas) {
        // Background line
        canvas.drawLine(
                mMarStartX,
                mMarStartY,
                mMarEndX,
                mMarEndY,
                mPaintBackground);

        canvas.drawLine(mMarStartX, mMarStartY, mStickX, mStickY, mPaintForeground);
        canvas.drawCircle(mStickX,mStickY, mPaintForeground.getStrokeWidth(), mPaintForeground);

        float x = mStickX - mStickBound;
        float y = mStickY - mStickBound;

        if (DEBUG_MODE) {
            canvas.drawRect(x, y,
                    mStickX + mStickBound,
                    mStickY + mStickBound,
                    mPaintDebug);

            canvas.drawText("Y: " + y, x, y - mPaintDebug.getTextSize(), mPaintDebug);
            canvas.drawText("X: " + x, x, y - mPaintDebug.getTextSize() * 2, mPaintDebug);
        }
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

    public void onLayout(int width, int height) {
        onLayout(width,height,
                mRandom.nextFloat(), mRandom.nextFloat(),
                mRandom.nextFloat(), mRandom.nextFloat());
        Log.d(TAG, "onLayout: Line::onLayout();");
    }

    public boolean checkCollide(float x, float y) {
        Log.d(TAG, "checkCollide: STICK_X: " + mStickX +
                " STICK_Y: " + mStickY +
                " STICK_BOUND: " + mStickBound +
                " X: " + x + " Y: " + y);
        return mStickX-mStickBound < x && x < mStickX+mStickBound &&
                mStickY-mStickBound < y && y < mStickY+mStickBound;
    }

    public byte onTouch(float x, float y) {
        Log.d(TAG, "onTouch: X: " + x + " " + y);
        if (mStickX-mStickBound < x && x < mStickX+mStickBound &&
                mStickY-mStickBound < y && y < mStickY+mStickBound) {

            float xAbs = Math.abs(mMarStartX-mMarEndX);
            float yAbs = Math.abs(mMarStartY-mMarEndY);

            if (yAbs < xAbs) {
                mStickX = x;
                mStickY = mMarStartY + (x - mMarStartX) / (mMarEndX - mMarStartX) * (mMarEndY - mMarStartY);

                if (mMarStartX < mMarEndX) {
                    if (x < mMarStartX) {
                        mStickX = mMarStartX;
                        mStickY = mMarStartY;
                    } else if (x > mMarEndX) {
                        mStickX = mMarEndX;
                        mStickY = mMarEndY;
                    }
                } else {
                    if (x < mMarEndX) {
                        mStickX = mMarEndX;
                        mStickY = mMarEndY;
                    } else if (x > mMarStartX) {
                        mStickX = mMarStartX;
                        mStickY = mMarStartY;
                    }
                }

                return DRAW_INVALIDATE_WITH_FALSE;
            }

            mStickX = mMarStartX + (y - mMarStartY) / (mMarEndY - mMarStartY) * (mMarEndX - mMarStartX);
            mStickY = y;
            if (mMarStartY < mMarEndY) {
                if (y < mMarStartY) {
                    mStickX = mMarStartX;
                    mStickY = mMarStartY;
                } else if (y > mMarEndY) {
                    mStickX = mMarEndX;
                    mStickY = mMarEndY;
                }
            } else {
                if (y < mMarEndY) {
                    mStickX = mMarEndX;
                    mStickY = mMarEndY;
                } else if (y > mMarStartY) {
                    mStickX = mMarStartX;
                    mStickY = mMarStartY;
                }
            }

            return DRAW_INVALIDATE_WITH_FALSE;
        }

        return DRAW_FALSE;
    }
}
