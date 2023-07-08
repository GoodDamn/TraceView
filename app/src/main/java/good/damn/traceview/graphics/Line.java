package good.damn.traceview.graphics;

import android.graphics.Canvas;
import android.util.Log;

public class Line extends Entity {

    private static final String TAG = "Line";

    private float deltaX = 0;
    private float deltaY = 0;

    private float mGradient = 0;

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

        canvas.drawLine(mStartXFor, mStartYFor, mStickX, mStickY, mPaintForeground);

        if (RELEASE_MODE) {
            return;
        }

        canvas.drawLine(mMarStartX + 10,
                mMarStartY,
                mMarEndX + 10,
                mMarEndY,
                mPaintDebug);

        canvas.drawLine(mMarStartX - 10,
                mMarStartY,
                mMarEndX - 10,
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

        Log.d(TAG, "onLayout: GRADIENT: " + mGradient);
        isXBigger = Math.abs(deltaY) < Math.abs(deltaX);

    }

    @Override
    public void onSetupPivotPoint(float x, float y) {

        float exp = y - mMarStartY - mGradient * (x - mMarStartX);

        Log.d(TAG, "onSetupPivotPoint: EXPRESSION: " + exp);

        if (!(-20 < exp && exp < 20)) {
            return;
        }

        if (Math.hypot(x-mMarEndX, y-mMarEndY) < 20
            || Math.hypot(x-mMarStartX, y-mMarStartY) < 20) {

            mStartXFor = x;
            mStartYFor = y;

            mStickX = mStartXFor;
            mStickY = mStartYFor;

            mHasPivot = true;
        }
    }

    @Override
    void onPlace(float x, float y) {

        if (isXBigger) {
            mStickX = x;

            mProgress = (x - mStartXFor) / deltaX;

            mStickY = mStartYFor + mProgress * deltaY;

            /*if (mMarStartX < mMarEndX) {
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
            }*/

            return;
        }

        mProgress = (y - mStartYFor) / deltaY;
        mStickX = mStartXFor + mProgress * deltaX;
        mStickY = y;
        /*if (mMarStartY < mMarEndY) {
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
        }*/
    }
}
