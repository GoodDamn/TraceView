package good.damn.marqueeview.graphics;

import android.app.appsearch.SearchResult;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.google.android.material.radiobutton.MaterialRadioButton;

public class Circle extends Entity {

    private static final String TAG = "Circle";

    private float mRadius;

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
                180,360*mProgress,
                false, mPaintForeground);
        canvas.drawCircle(mStickX, mStickY, mPaintBackground.getStrokeWidth(), mPaintForeground);
        super.onDraw(canvas); // debug mode
    }

    @Override
    public void onLayout(int width, int height, float startX, float startY, float endX, float endY) {
        super.onLayout(width, height, startX, startY, endX, endY);
        mRadius = (float) Math.hypot(mMarEndX-mMarStartX, mMarEndY-mMarStartY);
        mStickX = mMarStartX - mRadius;
    }

    @Override
    void onPlace(float x, float y) {
        // Shit-code below (mStickX, mStickY, mProgress)
        mProgress = Math.abs((x - (mMarStartX-mRadius)) / (mRadius+ mRadius));
        mStickX = x;
        Log.d(TAG, "onPlace: X: " + x + " START X: " + mMarStartX + " RADIUS: " + mRadius + " PROGRESS: " + mProgress);
    }
}
