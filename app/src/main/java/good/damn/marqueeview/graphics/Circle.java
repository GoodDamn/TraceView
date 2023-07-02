package good.damn.marqueeview.graphics;

import android.app.appsearch.SearchResult;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Circle extends Entity {

    private float mRadius;

    public Circle() {
        super();
        mPaintBackground.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawArc(mMarStartX-mRadius,
                mMarStartY-mRadius,
                mMarStartX+mRadius,
                mMarStartY+mRadius,
                0,360,
                true,mPaintBackground);
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
    float onXAxis() {
        return 0;
    }

    @Override
    float onYAxis() {
        return 0;
    }
}
