package good.damn.marqueeview.graphics;

import android.app.appsearch.SearchResult;
import android.graphics.Canvas;

public class Circle extends Entity {

    private float mRadius = 20.0f;

    public Circle() {
        super();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawCircle(mMarStartX-mRadius, mMarStartY-mRadius, mRadius, mPaintBackground);
        super.onDraw(canvas);
    }

    @Override
    public void onLayout(int width, int height, float startX, float startY, float endX, float endY) {
        super.onLayout(width, height, startX, startY, endX, endY);
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
