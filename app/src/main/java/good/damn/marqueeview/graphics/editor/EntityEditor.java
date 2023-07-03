package good.damn.marqueeview.graphics.editor;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

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

    public abstract EntityEditor copy();

    public abstract void draw(Canvas canvas, float fromX, float fromY, float toX, float toY);

}