package good.damn.marqueeview.graphics;

import android.app.appsearch.SearchResult;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.google.android.material.radiobutton.MaterialRadioButton;

public class Circle extends Entity {

    private static final String TAG = "Circle";

    private float mRadius;
    private float mAngle = 0;

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
                0,360*mProgress,
                false, mPaintForeground);

        super.onDraw(canvas); // debug mode
        if (RELEASE_MODE) {
            return;
        }




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
    void onPlace(float x, float y) {
        // Shit-code below (mStickX, mStickY, mProgress)

        float localX = x - mMarStartX;
        float localY = y - mMarStartY;

        float atan = (float) Math.atan2(localY,localX);
        float ang = (float) Math.toDegrees(atan);

        if (ang < 0) {
            ang = 360 + ang;
        }

        if (mProgress > 0.98f && ang < 90) { // marquee completed. Not again, please
            return;
        }

        if (ang > 135 && mAngle < 90) {
            ang = 1;
            atan = 0;
        }

        mAngle = ang;
        mProgress = mAngle / 360;

        float sin = (float) Math.sin(atan);
        float cos = (float) Math.cos(atan);

        mStickX = mMarStartX + mRadius*cos;
        mStickY = mMarStartY + mRadius*sin;
    }
}
