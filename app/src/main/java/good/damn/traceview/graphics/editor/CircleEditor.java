package good.damn.traceview.graphics.editor;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import good.damn.traceview.R;

public class CircleEditor extends EntityEditor{

    private final RectF mRectFArc = new RectF();

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
        mRectFArc.left = fromX-radius;
        mRectFArc.top = fromY-radius;
        mRectFArc.right = fromX+radius;
        mRectFArc.bottom = fromY+radius;

        canvas.drawArc(mRectFArc,0,360,false,mPaintBackground);

        canvas.drawCircle(fromX+radius,fromY,mPaintBackground.getStrokeWidth(),mPaintForeground);
    }
}
