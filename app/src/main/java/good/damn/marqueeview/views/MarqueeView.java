package good.damn.marqueeview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import good.damn.marqueeview.models.Line;

public class MarqueeView extends View implements View.OnTouchListener {

    private static final String TAG = "MarqueeView";

    private Line[] mLines;

    private Line mCurrentLineTouching;

    private void init() {

        mLines = new Line[10];

        for (byte i = 0; i < mLines.length;i++) {
            mLines[i] = new Line();
        }

        setOnTouchListener(this);
    }


    public MarqueeView(Context context) {
        super(context);
        init();
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Line mLine : mLines) {
            mLine.onDraw(canvas);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mLines[0].onLayout(getWidth(), getHeight(), 0.1f,0.3f,0.9f, 0.3f);

        // upper-triangle
        mLines[1].onLayout(getWidth(), getHeight(), 0.5f,0.1f,0.3f, 0.3f);
        mLines[2].onLayout(getWidth(), getHeight(), 0.5f,0.15f,0.7f, 0.3f);

        // lower triangle
        mLines[3].onLayout(getWidth(), getHeight(), 0.3f,0.3f,0.5f, 0.5f);
        mLines[4].onLayout(getWidth(), getHeight(), 0.5f,0.8f,0.5f, 0.5f);
        mLines[5].onLayout(getWidth(), getHeight(), 0.5f,0.5f,0.7f, 0.3f);

        // right side of triangle
        mLines[6].onLayout(getWidth(), getHeight(), 0.8f,0.5f,0.6f, 0.4f);
        mLines[7].onLayout(getWidth(), getHeight(), 0.8f,0.95f,0.8f, 0.5f);

        // left side of triangle
        mLines[8].onLayout(getWidth(), getHeight(), 0.2f,0.95f,0.2f, 0.5f);
        mLines[9].onLayout(getWidth(), getHeight(), 0.2f,0.5f,0.4f, 0.4f);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        Log.d(TAG, "onTouch: X: " + x + " Y: " + y);

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:

                for (Line mLine : mLines) {
                    if (mLine.checkCollide(x,y)) {
                        mCurrentLineTouching = mLine;
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                byte state = mCurrentLineTouching.onTouch(x,y);
                if (state == Line.DRAW_INVALIDATE_WITH_FALSE) {
                    invalidate();
                    return false;
                }

                if (state == Line.DRAW_FALSE) {
                    return false;
                }

                break;
        }

        return true;
    }
}