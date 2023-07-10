package good.damn.traceview.graphics;

import android.graphics.Canvas;
import android.util.Log;

import java.security.AlgorithmConstraints;

public class Line extends Entity {

    private static final String TAG = "Line";

    private float deltaX = 0;
    private float deltaY = 0;

    private float mStartXFor;
    private float mStartYFor;

    private float mGradient;

    private float mLineLength;
    private boolean mIsXBigger = false;
    private boolean mDoesItTouchPivot = false;

    @Override
    public void onDraw(Canvas canvas) {
        // Background line
        canvas.drawLine(
                mMarStartX,
                mMarStartY,
                mMarEndX,
                mMarEndY,
                mPaintBackground);

        if (mHasPivot) {
            canvas.drawLine(mStartXFor, mStartYFor, mStickX, mStickY, mPaintForeground);
        }

        if (!RELEASE_MODE) {
            return;
        }

        super.onDraw(canvas);

        if (mHasPivot) {
            canvas.drawRect(mStartXFor-mStickBound,
                    mStartYFor-mStickBound,
                    mStartXFor+mStickBound,
                    mStartYFor+mStickBound, mPaintDebug);
        }

        canvas.drawCircle(mStartXFor, mStartYFor, 1, mPaintDebug);
        canvas.drawCircle(mStartXFor, mStartYFor, 20, mPaintDebug);

        canvas.drawLine(mMarStartX,
                mMarStartY,
                mMarEndX,
                mMarEndY,
                mPaintDebug);

        canvas.drawCircle(mStartXFor, mStartYFor, 20,mPaintDebug);
    }

    @Override
    public void onLayout(int width, int height, float startX, float startY, float endX, float endY) {
        super.onLayout(width, height, startX, startY, endX, endY);
        deltaX = mMarEndX - mMarStartX;
        deltaY = mMarEndY - mMarStartY;

        mStartXFor = mMarStartX;
        mStartYFor = mMarStartY;

        mGradient = deltaY / deltaX;

        mLineLength = (float) Math.hypot(deltaX,deltaY);

        mIsXBigger = Math.abs(deltaY) < Math.abs(deltaX);
    }

    @Override
    public boolean checkCollide(float x, float y) {
        if (mHasPivot && boxBound(x,y,mStartXFor, mStartYFor)) {
            mDoesItTouchPivot = true;
            return true;
        }

        return boxBound(x,y,mStickX,mStickY);
    }

    @Override
    boolean checkDeltaInBounds(float x, float y) {
        if (mDoesItTouchPivot) {
            mDoesItTouchPivot = boxBound(x,y,mStartXFor,mStartYFor);
            return mDoesItTouchPivot;
        }

        return boxBound(x,y,mStickX,mStickY);
    }

    @Override
    public void onSetupPivotPoint(float x, float y) {

        float dy = y - mMarStartY;
        float dx = x - mMarStartX;

        float eq = dy - mGradient * dx;

        Log.d(TAG, "onSetupPivotPoint: EXPRESSION: " + eq);

        if (-50 < eq && eq < 50) {
            if (mIsXBigger) {
                mStartXFor = x;
                mStartYFor = dx / deltaX * deltaY + mMarStartY;
            } else {
                mStartXFor = dy / deltaY * deltaX + mMarStartX;
                mStartYFor = y;
            }

            mStickX = mStartXFor;
            mStickY = mStartYFor;

            mHasPivot = true;
        }
    }

    @Override
    public void onTouchUp() {
        mDoesItTouchPivot = false;
    }

    @Override
    void onPlace(float x, float y) {
        if (mDoesItTouchPivot) {
            if (mIsXBigger) {
                mStartXFor = x;
                mStartYFor = mMarStartY + (x - mMarStartX) / deltaX * deltaY;
            } else {
                mStartXFor = mMarStartX + (y - mMarStartY) / deltaY * deltaX;
                mStartYFor = y;
            }
            float length = (float) Math.hypot(mStartXFor-mStickX, mStartYFor-mStickY);
            mProgress = length / mLineLength;
            Log.d(TAG, "onPlace: PROGRESS::"+mProgress + " LENGTH: " + length + " LINE_LENGTH:" + mLineLength);
            return;
        }

        if (mIsXBigger) {
            mStickY = mMarStartY + (x - mMarStartX) / deltaX * deltaY;
            mStickX = x;
        } else {
            mStickX = mMarStartX + (y - mMarStartY) / deltaY * deltaX;
            mStickY = y;
        }

        float length = (float) Math.hypot(mStartXFor-mStickX, mStartYFor-mStickY);
        mProgress = length / mLineLength;
        Log.d(TAG, "onPlace: PROGRESS::"+mProgress + " LENGTH: " + length + " LINE_LENGTH:" + mLineLength);

    }
}
