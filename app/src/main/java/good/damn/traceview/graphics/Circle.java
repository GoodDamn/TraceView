package good.damn.traceview.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import good.damn.traceview.utils.Maths;

public class Circle extends Entity {

    private static final String TAG = "Circle";

    private float mRadius;
    private float mAngle = 0;
    private float mStartAngle = 0;
    private float mStartAngleRadians = 0;

    public Circle() {
        super();
        mPaintBackground.setStyle(Paint.Style.STROKE);
        mPaintForeground.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawArc(mMarStartX-mRadius,
                mMarStartY-mRadius,
                mMarStartX+mRadius,
                mMarStartY+mRadius,
                0,360,
                false,mPaintBackground);
        canvas.drawArc(mMarStartX-mRadius,
                mMarStartY-mRadius,
                mMarStartX+mRadius,
                mMarStartY+mRadius,
                mStartAngle,360*mProgress,
                false, mPaintForeground);

        if (!RELEASE_MODE) {
            return;
        }

        super.onDraw(canvas); // debug mode

        canvas.drawCircle(mMarStartX, mMarStartY, 5, mPaintDebug);
        canvas.drawLine(mMarStartX-mRadius,mMarStartY,mMarStartX+mRadius,mMarStartY,mPaintDebug);
        canvas.drawLine(mMarStartX,mMarStartY-mRadius,mMarStartX,mMarStartY+mRadius,mPaintDebug);

        canvas.drawText("ANG: " + mAngle, mStickX-mStickBound,mStickY-mStickBound-mPaintDebug.getTextSize()*4, mPaintDebug);
    }

    @Override
    public void onLayout(int width, int height, float startX, float startY, float endX, float endY) {
        super.onLayout(width, height, startX, startY, endX, endY);
        mRadius = (float) Math.hypot(mMarEndX-mMarStartX, mMarEndY-mMarStartY);
        mStickX = mMarStartX + mRadius;
    }

    @Override
    public void reset() {
        super.reset();
        mStickX = mMarStartX + mRadius;
        mAngle = 0;
    }

    @Override
    public void onSetupPivotPoint(float gx, float gy) {
        float x = gx - mMarStartX;
        float y = gy - mMarStartY;
        float radius = x * x + y * y;
        float r = mPaintBackground.getStrokeWidth() / 2;
        Log.d(TAG, "onSetupPivotPoint: -------------------");
        Log.d(TAG, "onSetupPivotPoint: X: " +x + " Y:" + y);
        Log.d(TAG, "onSetupPivotPoint: RAD-5: " + Maths.sqr(mRadius-r) + " RAD: "+ radius +" RAD+5: " + Maths.sqr(mRadius+r));
        Log.d(TAG, "onSetupPivotPoint: -------------------");
        if (Maths.sqr(mRadius-r) < radius && radius < Maths.sqr(mRadius+r)) {

            float ang = (float) Math.atan2(y,x);
            mStartAngle = (float) Math.toDegrees(ang);

            mStartAngleRadians = (float) Math.toRadians(mStartAngle);

            mStickX = (float) (mMarStartX + mRadius * Math.cos(ang));
            mStickY = (float) (mMarStartY + mRadius * Math.sin(ang));

            Log.d(TAG, "onSetupPivotPoint: HAS PIVOT: " + mStartAngle);

            mHasPivot = true;
        }
    }

    @Override
    void onPlace(float x, float y) {
        // Shit-code below (mStickX, mStickY, mProgress)

        float lX = x - mMarStartX;
        float lY = y - mMarStartY;

        float s = (float) Math.sin(mStartAngleRadians);
        float c = (float) Math.cos(mStartAngleRadians);

        float localX = (float) (lX * c + lY * s);
        float localY = (float) (-lX * s + lY * c);

        float rad = (float) (Math.atan2(localY,localX));
        float ang = (float) Math.toDegrees(rad);

        if (ang < 0) {
            ang = 360 + ang;
        }

        if (mProgress > 0.98f && ang < 90) { // trace is completed. Not again, please
            return;
        }

        if (ang > 135 && mAngle < 90) {
            ang = 1;
        }

        mAngle = ang;
        mProgress = mAngle / 360;

        rad = (float) Math.toRadians(mStartAngle+mAngle);

        s = (float) Math.sin(rad);
        c = (float) Math.cos(rad);

        mStickX = mMarStartX + mRadius*c;
        mStickY = mMarStartY + mRadius*s;
    }
}
