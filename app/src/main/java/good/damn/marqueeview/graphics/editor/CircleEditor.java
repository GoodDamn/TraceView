package good.damn.marqueeview.graphics.editor;

import android.graphics.Canvas;
import android.graphics.Paint;

public class CircleEditor extends EntityEditor{

    public CircleEditor(Paint fore, Paint back) {
        super(fore, back);
    }

    @Override
    public EntityEditor copy() {
        return new CircleEditor(mPaintForeground,mPaintBackground);
    }

    @Override
    public void draw(Canvas canvas, float fromX, float fromY, float toX, float toY) {
        canvas.drawCircle(fromX,fromY, (float) Math.hypot(fromX-toX,fromY-toY),mPaintBackground);
        canvas.drawCircle(fromX,fromY,mPaintBackground.getStrokeWidth(),mPaintForeground);
    }
}
