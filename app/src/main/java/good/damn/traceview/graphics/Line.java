package good.damn.traceview.graphics;

import android.graphics.Canvas;

public class Line extends Entity {

    private static final String TAG = "Line";

    @Override
    public void onDraw(Canvas canvas) {
        // Background line
        canvas.drawLine(
                mMarStartX,
                mMarStartY,
                mMarEndX,
                mMarEndY,
                mPaintBackground);

        canvas.drawLine(mMarStartX, mMarStartY, mStickX, mStickY, mPaintForeground);
    }

    @Override
    void onPlace(float x, float y) {
        float xAbs = Math.abs(mMarStartX-mMarEndX);
        float yAbs = Math.abs(mMarStartY-mMarEndY);

        if (yAbs < xAbs) {
            mStickX = x;

            mProgress = (x - mMarStartX) / (mMarEndX - mMarStartX);

            mStickY = mMarStartY + mProgress * (mMarEndY - mMarStartY);

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

            return;
        }

        mProgress = (y - mMarStartY) / (mMarEndY - mMarStartY);
        mStickX = mMarStartX + mProgress * (mMarEndX - mMarStartX);
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
    }
}
