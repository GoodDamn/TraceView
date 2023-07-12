package good.damn.traceview.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import good.damn.traceview.utils.Maths;

public class Circle extends Entity {

    private static final String TAG = "Circle";

    private final RectF mRectFArc = new RectF();

    private float mRadius;
    private float mAngle = 0;
    private float mStartAngle = 0;
    private float mStartAngleRadians = 0;

    private float mStartAngleSin = 0;
    private float mStartAngleCos = 0;

    private float mPivotPointTrigger = 5.0f;


    public Circle() {
        mPaintBackground.setStyle(Paint.Style.STROKE);
        mPaintForeground.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawArc(mRectFArc, 0,360, false,mPaintBackground);
        canvas.drawArc(mRectFArc, mStartAngle,360*mProgress, false, mPaintForeground);

        if (RELEASE_MODE) {
            return;
        }

        super.onDraw(canvas); // debug mode

        canvas.drawCircle(mTraceStartX, mTraceStartY, 5, mPaintDebug);
        canvas.drawLine(mTraceStartX -mRadius, mTraceStartY, mTraceStartX +mRadius, mTraceStartY,mPaintDebug);
        canvas.drawLine(mTraceStartX, mTraceStartY -mRadius, mTraceStartX, mTraceStartY +mRadius,mPaintDebug);

        canvas.drawCircle(mTraceStartX, mTraceStartY, mRadius-mPivotPointTrigger, mPaintDebug);
        canvas.drawCircle(mTraceStartX, mTraceStartY, mRadius+mPivotPointTrigger, mPaintDebug);

        canvas.drawText("ANG: " + mAngle, mStickX-mStickBound,mStickY-mStickBound-mPaintDebug.getTextSize()*4, mPaintDebug);
    }

    @Override
    public void onLayout(int width, int height) {
        super.onLayout(width, height);
        mRadius = (float) Math.hypot(mTraceEndX - mTraceStartX, mTraceEndY - mTraceStartY);
        mStickX = mTraceStartX + mRadius;
        mPivotPointTrigger = mPaintBackground.getStrokeWidth() * 1.5f;

        mRectFArc.left = mTraceStartX -mRadius;
        mRectFArc.top = mTraceStartY -mRadius;
        mRectFArc.right = mTraceStartX +mRadius;
        mRectFArc.bottom = mTraceStartY +mRadius;
    }

    @Override
    public void reset() {
        super.reset();
        mStickX = mTraceStartX + mRadius;
        mAngle = 0;
    }

    @Override
    public void onSetupPivotPoint(float gx, float gy) {
        float x = gx - mTraceStartX;
        float y = gy - mTraceStartY;
        float radius = x * x + y * y;
        if (Maths.sqr(mRadius-mPivotPointTrigger) < radius
                && radius < Maths.sqr(mRadius+mPivotPointTrigger)) {

            float ang = (float) Math.atan2(y,x);
            mStartAngle = (float) Math.toDegrees(ang);

            mStartAngleRadians = (float) Math.toRadians(mStartAngle);

            mStartAngleSin = (float) Math.sin(mStartAngleRadians);
            mStartAngleCos = (float) Math.cos(mStartAngleRadians);

            mStickX = (float) (mTraceStartX + mRadius * Math.cos(ang));
            mStickY = (float) (mTraceStartY + mRadius * Math.sin(ang));

            Log.d(TAG, "onSetupPivotPoint: HAS PIVOT: " + mStartAngle);

            mHasPivot = true;
        }
    }

    @Override
    public boolean checkCollide(float x, float y) {
        return boxBound(x,y,mStickX,mStickY);
    }


    @Override
    boolean checkDeltaInBounds(float x, float y) {
        return checkCollide(x,y);
    }

    @Override
    void onPlace(float x, float y) {
        // Shit-code below (mStickX, mStickY, mProgress)

        // Project to local coordinate system
        float lX = x - mTraceStartX;
        float lY = y - mTraceStartY;

        // Project to local rotated coordinate system
        float localX = lX * mStartAngleCos + lY * mStartAngleSin;
        float localY = -lX * mStartAngleSin + lY * mStartAngleCos;

        // Get angle
        float rad = (float) (Math.atan2(localY,localX));
        float ang = (float) Math.toDegrees(rad);

        // Check sides
        if (ang < 0 && mAngle > 80) {
            ang = 360 + ang;
        } else if (ang > 0 && mAngle < -80) {
            ang = -180 - (180-ang);
        }

        mAngle = ang;
        mProgress = mAngle / 360;

        rad = (float) Math.toRadians(mStartAngle+mAngle);

        // Rotate stick depend on mAngle on global coordinate system
        mStickX = (float) (mTraceStartX + mRadius*Math.cos(rad));
        mStickY = (float) (mTraceStartY + mRadius*Math.sin(rad));
    }

    @Override
    public void onPrepareAnimation() {
        mHasPivot = true;
    }

    @Override
    public void onAnimate(float progress) {
        mProgress = progress;
    }
}
