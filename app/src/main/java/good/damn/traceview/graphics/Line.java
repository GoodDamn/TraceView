package good.damn.traceview.graphics;

import android.graphics.Canvas;
import android.util.Log;

public class Line extends Entity {

    private static final String TAG = "Line";

    private float deltaX = 0;
    private float deltaY = 0;

    private float mStartXFor;
    private float mStartYFor;

    private float minX;
    private float minY;
    private float maxX;
    private float maxY;

    private float mGradient;

    private float mLineLength;
    private boolean mIsXBigger = false;
    private boolean mDoesItTouchPivot = false;

    @Override
    public void onDraw(Canvas canvas) {
        // Background line
        canvas.drawLine(
                mTraceStartX,
                mTraceStartY,
                mTraceEndX,
                mTraceEndY,
                mPaintBackground);

        if (mHasPivot) {
            canvas.drawLine(mStartXFor, mStartYFor, mStickX, mStickY, mPaintForeground);
        }

        if (RELEASE_MODE) {
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

        canvas.drawLine(mTraceStartX,
                mTraceStartY,
                mTraceEndX,
                mTraceEndY,
                mPaintDebug);

        canvas.drawCircle(mStartXFor, mStartYFor, 20,mPaintDebug);
    }

    @Override
    public void onLayout(int width, int height) {
        super.onLayout(width, height);
        deltaX = mTraceEndX - mTraceStartX;
        deltaY = mTraceEndY - mTraceStartY;

        mStartXFor = mTraceStartX;
        mStartYFor = mTraceStartY;

        mStickX = mStartXFor;
        mStickY = mStartYFor;

        mGradient = deltaY / deltaX;

        Log.d(TAG, "onLayout: GRADIENT: " + mGradient);

        mLineLength = (float) Math.hypot(deltaX,deltaY);

        mIsXBigger = Math.abs(deltaY) < Math.abs(deltaX);

        minX = Math.min(mTraceStartX, mTraceEndX);
        minY = Math.min(mTraceStartY, mTraceEndY);

        maxX = Math.max(mTraceStartX, mTraceEndX);
        maxY = Math.max(mTraceStartY, mTraceEndY);
    }

    @Override
    public boolean checkCollide(float x, float y) {

        if (!mHasPivot) {
            return false;
        }

        if (boxBound(x,y,mStartXFor, mStartYFor)) {
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

        byte s = (byte) mPaintForeground.getStrokeWidth();

        if (!(minX-s < x && x < maxX+s
                && minY-s < y && y < maxY+s)
        ) { // Out of bounds
            return;
        }

        float dy = y - mTraceStartY;
        float dx = x - mTraceStartX;

        if (Math.abs(mGradient) > 15) { // Y is longer(bigger)
            mStartXFor = dy / deltaY * deltaX + mTraceStartX;
            mStartYFor = y;

            mStickX = mStartXFor;
            mStickY = mStartYFor;

            mHasPivot = true;
            return;
        }

        float eq = dy - mGradient * dx;

        Log.d(TAG, "onSetupPivotPoint: EXPRESSION: " + eq);

        if (-50 < eq && eq < 50) {
            if (mIsXBigger) {
                mStartXFor = x;
                mStartYFor = dx / deltaX * deltaY + mTraceStartY;
            } else {
                mStartXFor = dy / deltaY * deltaX + mTraceStartX;
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
                mStartYFor = mTraceStartY + (x - mTraceStartX) / deltaX * deltaY;

                //////////////////////////////!!!!!!
                if (mTraceStartX < mTraceEndX) {
                    if (x < mTraceStartX) {
                        mStartXFor = mTraceStartX;
                        mStartYFor = mTraceStartY;
                    } else if (x > mTraceEndX) {
                        mStartXFor = mTraceEndX;
                        mStartYFor = mTraceEndY;
                    }

                } else {
                    if (x < mTraceEndX) {
                        mStartXFor = mTraceEndX;
                        mStartYFor = mTraceEndY;
                    } else if (x > mTraceStartX) {
                        mStartXFor = mTraceStartX;
                        mStartYFor = mTraceStartY;
                    }
                }
                ////////////////////////!!!!!

            } else {
                mStartXFor = mTraceStartX + (y - mTraceStartY) / deltaY * deltaX;
                mStartYFor = y;

                //////////////////////////////!!!!!!
                if (mTraceStartY < mTraceEndY) {
                    if (y < mTraceStartY) {
                        mStartXFor = mTraceStartX;
                        mStartYFor = mTraceStartY;
                    } else if (y > mTraceEndY) {
                        mStartXFor = mTraceEndX;
                        mStartYFor = mTraceEndY;
                    }

                } else {
                    if (y < mTraceEndY) {
                        mStartXFor = mTraceEndX;
                        mStartYFor = mTraceEndY;
                    } else if (y > mTraceStartY) {
                        mStartXFor = mTraceStartX;
                        mStartYFor = mTraceStartY;
                    }
                }
                ////////////////////////!!!!!

            }


            float length = (float) Math.hypot(mStartXFor-mStickX, mStartYFor-mStickY);
            mProgress = length / mLineLength;
            Log.d(TAG, "onPlace: PROGRESS::"+mProgress + " LENGTH: " + length + " LINE_LENGTH:" + mLineLength);
            return;
        }

        // REFACTOR SHIT CODE WITH CHECKING COLLISION LIMIT!!!!!!

        if (mIsXBigger) {
            mStickY = mTraceStartY + (x - mTraceStartX) / deltaX * deltaY;
            mStickX = x;

            //////////////////////////////!!!!!!
            if (mTraceStartX < mTraceEndX) {
                if (x < mTraceStartX) {
                    mStickX = mTraceStartX;
                    mStickY = mTraceStartY;
                } else if (x > mTraceEndX) {
                    mStickX = mTraceEndX;
                    mStickY = mTraceEndY;
                }

            } else {
                if (x < mTraceEndX) {
                    mStickX = mTraceEndX;
                    mStickY = mTraceEndY;
                } else if (x > mTraceStartX) {
                    mStickX = mTraceStartX;
                    mStickY = mTraceStartY;
                }
            }
            ////////////////////////!!!!!

        } else {
            mStickX = mTraceStartX + (y - mTraceStartY) / deltaY * deltaX;
            mStickY = y;

            //////////////////////////////!!!!!!
            if (mTraceStartY < mTraceEndY) {
                if (y < mTraceStartY) {
                    mStickX = mTraceStartX;
                    mStickY = mTraceStartY;
                } else if (y > mTraceEndY) {
                    mStickX = mTraceEndX;
                    mStickY = mTraceEndY;
                }

            } else {
                if (y < mTraceEndY) {
                    mStickX = mTraceEndX;
                    mStickY = mTraceEndY;
                } else if (y > mTraceStartY) {
                    mStickX = mTraceStartX;
                    mStickY = mTraceStartY;
                }
            }
            ////////////////////////!!!!!

        }

        float length = (float) Math.hypot(mStartXFor-mStickX, mStartYFor-mStickY);
        mProgress = length / mLineLength;
        Log.d(TAG, "onPlace: PROGRESS::"+mProgress + " LENGTH: " + length + " LINE_LENGTH:" + mLineLength);

    }

    @Override
    public void onPrepareAnimation() {
        mHasPivot = true;
    }

    @Override
    public void onAnimate(float progress) {
        mStickX = mStartXFor + progress * deltaX;
        mStickY = mStartYFor + progress * deltaY;
    }
}
