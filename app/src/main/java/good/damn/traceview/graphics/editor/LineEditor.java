package good.damn.traceview.graphics.editor;

import android.graphics.Canvas;
import android.graphics.Paint;

public class LineEditor extends EntityEditor {

    public LineEditor(Paint fore, Paint back) {
        super(fore, back);
    }

    @Override
    public EntityEditor copy() {
        return new LineEditor(mPaintForeground,mPaintBackground);
    }

    @Override
    public void draw(Canvas canvas, float fromX, float fromY, float toX, float toY) {
        canvas.drawLine(fromX, fromY, toX, toY, mPaintBackground);
        canvas.drawCircle(fromX, fromY, mPaintBackground.getStrokeWidth(),mPaintForeground);
    }
}
