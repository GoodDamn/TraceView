package good.damn.marqueeview.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;

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

        super.onDraw(canvas);
    }

    @Override
    public void onLayout(int width, int height,
                         float startX, float startY,
                         float endX, float endY) {
        super.onLayout(width, height, startX, startY, endX, endY);
    }

    @Override
    float onXAxis() {
        return mMarStartX + mProgress * (mMarEndX - mMarStartX);
    }

    @Override
    float onYAxis() {
        return mMarStartY + mProgress * (mMarEndY - mMarStartY);
    }
}
