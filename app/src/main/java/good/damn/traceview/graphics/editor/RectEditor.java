package good.damn.traceview.graphics.editor;

import android.graphics.Canvas;
import android.graphics.Paint;

import good.damn.traceview.graphics.Entity;

public class RectEditor extends EntityEditor {

    public RectEditor(Paint fore, Paint back) {
        super(fore, back);
    }

    @Override
    public EntityEditor copy() {
        return new RectEditor(mPaintForeground,mPaintBackground);
    }

    private void drawLine(float startX,float startY,float endX,float endY, Canvas canvas) {
        canvas.drawLine(startX,startY,endX,endY,mPaintBackground);
        canvas.drawCircle(startX,startY,mPaintBackground.getStrokeWidth(),mPaintForeground);
    }

    @Override
    public void draw(Canvas canvas, float fromX, float fromY, float toX, float toY) {
        float radius = (float) Math.hypot(toX-fromX,toY-fromY);

        float leftX = fromX-radius;
        float topY = fromY-radius;

        float rightX = fromX+radius;
        float bottomY = fromY+radius;

        drawLine(leftX,topY,rightX,topY,canvas);
        drawLine(rightX,topY,rightX,bottomY,canvas);
        drawLine(rightX,bottomY,leftX,bottomY,canvas);
        drawLine(leftX,bottomY,leftX,topY,canvas);
    }
}
