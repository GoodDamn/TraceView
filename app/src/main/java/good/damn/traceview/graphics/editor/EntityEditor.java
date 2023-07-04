package good.damn.traceview.graphics.editor;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.ColorInt;

public abstract class EntityEditor {

    protected Paint mPaintBackground = new Paint();
    protected Paint mPaintForeground = new Paint();

    public EntityEditor(Paint fore, Paint back) {
        mPaintBackground.setStrokeWidth(back.getStrokeWidth());
        mPaintBackground.setColor(back.getColor());
        mPaintBackground.setStrokeCap(Paint.Cap.ROUND);

        mPaintForeground.setStrokeCap(Paint.Cap.ROUND);
        mPaintForeground.setColor(fore.getColor());
    }

    public void setColor(@ColorInt int color) {
        mPaintForeground.setColor(color);
    }

    public int getColor() {
        return mPaintForeground.getColor();
    }

    public byte getStrokeWidth() {
        return (byte) mPaintBackground.getStrokeWidth();
    }

    public abstract EntityEditor copy();

    public abstract void draw(Canvas canvas, float fromX, float fromY, float toX, float toY);

}
