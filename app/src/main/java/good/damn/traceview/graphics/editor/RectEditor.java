package good.damn.traceview.graphics.editor;

import android.graphics.Canvas;
import android.graphics.Paint;

import good.damn.traceview.graphics.Entity;

public class RectEditor extends EntityEditor {

    private final float[][] mPoints;

    private void drawLine(float startX,float startY,float endX,float endY, int i, Canvas canvas) {
        canvas.drawLine(startX,startY,endX,endY,mPaintBackground);
        canvas.drawCircle(startX,startY,mPaintBackground.getStrokeWidth(),mPaintForeground);
        mPoints[i][0] = startX;
        mPoints[i][1] = startY;
        mPoints[i][2] = endX;
        mPoints[i][3] = endY;
    }

    public RectEditor(Paint fore, Paint back) {
        super(fore, back);
        mPoints = new float[4][4];
    }

    public float[][] getPoints() {
        return mPoints;
    }

    @Override
    public EntityEditor copy() {
        return new RectEditor(mPaintForeground,mPaintBackground);
    }

    @Override
    public void draw(Canvas canvas, float fromX, float fromY, float toX, float toY) {
        float radius = (float) Math.hypot(toX-fromX,toY-fromY);

        float leftX = fromX-radius;
        float topY = fromY-radius;

        float rightX = fromX+radius;
        float bottomY = fromY+radius;

        drawLine(leftX,topY,rightX,topY,0,canvas);
        drawLine(rightX,topY,rightX,bottomY,1,canvas);
        drawLine(rightX,bottomY,leftX,bottomY,2,canvas);
        drawLine(leftX,bottomY,leftX,topY,3,canvas);
    }
}
