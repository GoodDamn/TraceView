package good.damn.traceview.graphics.editor;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.ColorInt;

public abstract class EntityEditor {

    protected Paint mPaintBackground = new Paint();
    protected Paint mPaintForeground = new Paint();

    private float mStartNormalX;
    private float mStartNormalY;
    private float mEndNormalX;
    private float mEndNormalY;

    private int mAnimDuration;

    public EntityEditor(Paint fore, Paint back) {
        mPaintBackground.setStrokeWidth(back.getStrokeWidth());
        mPaintBackground.setColor(back.getColor());
        mPaintBackground.setStrokeCap(Paint.Cap.ROUND);

        mPaintForeground.setStrokeCap(Paint.Cap.ROUND);
        mPaintForeground.setColor(fore.getColor());
        mAnimDuration = 1000; // in millis
    }

    public void setColor(@ColorInt int color) {
        mPaintForeground.setColor(color);
    }

    public void setStrokeWidth(byte strokeWidth) {
        mPaintBackground.setStrokeWidth(strokeWidth);
    }

    public void setStartNormalPoint(float startX, float startY) {
        mStartNormalX = startX;
        mStartNormalY = startY;
    }

    public void setEndNormalPoint(float endX, float endY) {
        mEndNormalX = endX;
        mEndNormalY = endY;
    }

    public void setDuration(int duration) {
        mAnimDuration = duration;
    }

    public float getStartNormalX() {
        return mStartNormalX;
    }

    public float getStartNormalY() {
        return mStartNormalY;
    }

    public float getEndNormalX() {
        return mEndNormalX;
    }

    public float getEndNormalY() {
        return mEndNormalY;
    }

    public int getDuration() {
        return mAnimDuration;
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
