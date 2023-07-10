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
        super();
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

        canvas.drawCircle(mMarStartX, mMarStartY, 5, mPaintDebug);
        canvas.drawLine(mMarStartX-mRadius,mMarStartY,mMarStartX+mRadius,mMarStartY,mPaintDebug);
        canvas.drawLine(mMarStartX,mMarStartY-mRadius,mMarStartX,mMarStartY+mRadius,mPaintDebug);

        canvas.drawCircle(mMarStartX, mMarStartY, mRadius-mPivotPointTrigger, mPaintDebug);
        canvas.drawCircle(mMarStartX, mMarStartY, mRadius+mPivotPointTrigger, mPaintDebug);

        canvas.drawText("ANG: " + mAngle, mStickX-mStickBound,mStickY-mStickBound-mPaintDebug.getTextSize()*4, mPaintDebug);
    }

    @Override
    public void onLayout(int width, int height, float startX, float startY, float endX, float endY) {
        super.onLayout(width, height, startX, startY, endX, endY);
        mRadius = (float) Math.hypot(mMarEndX-mMarStartX, mMarEndY-mMarStartY);
        mStickX = mMarStartX + mRadius;
        mPivotPointTrigger = mPaintBackground.getStrokeWidth() * 1.5f;

        mRectFArc.left = mMarStartX-mRadius;
        mRectFArc.top = mMarStartY-mRadius;
        mRectFArc.right = mMarStartX+mRadius;
        mRectFArc.bottom = mMarStartY+mRadius;
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
        if (Maths.sqr(mRadius-mPivotPointTrigger) < radius
                && radius < Maths.sqr(mRadius+mPivotPointTrigger)) {

            float ang = (float) Math.atan2(y,x);
            mStartAngle = (float) Math.toDegrees(ang);

            mStartAngleRadians = (float) Math.toRadians(mStartAngle);

            mStartAngleSin = (float) Math.sin(mStartAngleRadians);
            mStartAngleCos = (float) Math.cos(mStartAngleRadians);

            mStickX = (float) (mMarStartX + mRadius * Math.cos(ang));
            mStickY = (float) (mMarStartY + mRadius * Math.sin(ang));

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
        float lX = x - mMarStartX;
        float lY = y - mMarStartY;

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
        mStickX = (float) (mMarStartX + mRadius*Math.cos(rad));
        mStickY = (float) (mMarStartY + mRadius*Math.sin(rad));
    }
}
