package good.damn.traceview.graphics;

import android.graphics.Canvas;
import android.util.Log;

public class Line extends Entity {

    private static final String TAG = "Line";

    private float deltaX = 0;
    private float deltaY = 0;

    private float mStartXFor;
    private float mStartYFor;

    private boolean isXBigger = false;

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

        if (RELEASE_MODE) {
            return;
        }

        super.onDraw(canvas);

        canvas.drawRect(mMarStartX-mStickBound,
                mMarStartY-mStickBound,
                    mMarStartX+mStickBound,
                    mMarStartY+mStickBound, mPaintDebug);

        canvas.drawRect(mMarEndX-mStickBound,
                mMarEndY-mStickBound,
                mMarEndX+mStickBound,
                mMarEndY+mStickBound, mPaintDebug);

        canvas.drawCircle(mStartXFor, mStartYFor, 20,mPaintDebug);
    }

    @Override
    public void onLayout(int width, int height, float startX, float startY, float endX, float endY) {
        super.onLayout(width, height, startX, startY, endX, endY);
        deltaX = mMarEndX - mMarStartX;
        deltaY = mMarEndY - mMarStartY;

        mStartXFor = mMarStartX;
        mStartYFor = mMarStartY;

        isXBigger = Math.abs(deltaY) < Math.abs(deltaX);
    }

    @Override
    public void onSetupPivotPoint(float x, float y) {

        /*float exp = y - mMarStartY - mGradient * (x - mMarStartX);

        Log.d(TAG, "onSetupPivotPoint: EXPRESSION: " + exp);

        if (!(-20 < exp && exp < 20)) {
            return;
        }*/

        if (mMarEndX-mStickBound < x && x < mMarEndX+mStickBound &&
            mMarEndY-mStickBound < y && y < mMarEndY+mStickBound) {

            mStartXFor = mMarEndX;
            mStartYFor = mMarEndY;

            mStickX = mStartXFor;
            mStickY = mStartYFor;

            mHasPivot = true;
        } else if (mMarStartX-mStickBound < x && x < mMarStartX+mStickBound &&
                mMarStartY-mStickBound < y && y < mMarStartY+mStickBound) {
            mStartXFor = mMarStartX;
            mStartYFor = mMarStartY;

            mStickX = mStartXFor;
            mStickY = mStartYFor;

            mHasPivot = true;
        }
    }

    @Override
    void onPlace(float x, float y) {

        if (isXBigger) {
            mProgress = (x - mStartXFor) / deltaX;

            Log.d(TAG, "onPlace: PROGRESS_DELTA_X_CHECK: >1.0f: " + (mProgress > 1.0f) + " <-1.0f: " + (mProgress < -1.0f) + " PROGRESS: " + mProgress);
            if (mProgress > 1.0f) {
                mProgress = 1.0f;
                x = mStickX;
            } else if (mProgress < -1.0f) {
                mProgress = -1.0f;
                x = mStickX;
            }

            Log.d(TAG, "onPlace: PROGRESS_DELTA_X: " + mProgress + " START_Y: " + mStartXFor);

            mStickY = mStartYFor + mProgress * deltaY;
            mStickX = x;
            return;
        }

        mProgress = (y - mStartYFor) / deltaY;

        Log.d(TAG, "onPlace: PROGRESS_DELTA_Y_CHECK: >1.0f: " + (mProgress > 1.0f) + " <-1.0f: " + (mProgress < -1.0f) + " PROGRESS: " + mProgress);

        if (mProgress > 1.0f) {
            mProgress = 1.0f;
            y = mStickY;
        } else if (mProgress < -1.0f) {
            mProgress = -1.0f;
            y = mStickY;
        }

        Log.d(TAG, "onPlace: PROGRESS_DELTA_Y: " + mProgress + " START_X: " + mStartXFor);

        mStickX = mStartXFor + mProgress * deltaX;
        mStickY = y;
    }
}
