package good.damn.traceview.graphics.editor;

import android.graphics.Canvas;
import android.graphics.Paint;

public class CircleEditor extends EntityEditor{

    public CircleEditor(Paint fore, Paint back) {
        super(fore, back);
        mPaintBackground.setStyle(Paint.Style.STROKE);
    }

    @Override
    public EntityEditor copy() {
        return new CircleEditor(mPaintForeground,mPaintBackground);
    }

    @Override
    public void draw(Canvas canvas, float fromX, float fromY, float toX, float toY) {
        float radius = (float) Math.hypot(toX-fromX,toY-fromY);
        canvas.drawArc(fromX-radius,
                fromY-radius,
                fromX+radius,
                fromY+radius,
                0,360,false,mPaintBackground);

        canvas.drawCircle(fromX+radius,fromY,mPaintBackground.getStrokeWidth(),mPaintForeground);
    }
}
