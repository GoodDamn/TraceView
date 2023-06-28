package good.damn.marqueeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.LinkedList;

public class MarqueeEditorView extends View implements View.OnTouchListener {

    private static final String TAG = "MarqueeEditorView";

    private final Paint mPaint = new Paint();
    private final Paint mPaintCircle = new Paint();

    private final LinkedList<LinePosition> mLinePositions = new LinkedList<>();

    private float mFromX;
    private float mFromY;

    private float mToX;
    private float mToY;


    private void init() {

        mPaint.setColor(0x55ffffff);
        mPaint.setStrokeWidth(15.0f);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaintCircle.setColor(0xff00ff59);

        setOnTouchListener(this);
    }

    public MarqueeEditorView(Context context) {
        super(context);
        init();
    }

    public MarqueeEditorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarqueeEditorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (LinePosition position: mLinePositions) {
            canvas.drawLine(position.fromX, position.fromY, position.toX, position.toY, mPaint);
            canvas.drawCircle(position.fromX, position.fromY, mPaint.getStrokeWidth(),mPaintCircle);
        }

        canvas.drawLine(mFromX, mFromY, mToX, mToY, mPaint);
        canvas.drawCircle(mFromX, mFromY, mPaint.getStrokeWidth(),mPaintCircle);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (event.getX() > getWidth() - 100 && event.getY() < 100) { // Undo previous action
                    if (mLinePositions.size() != 0) {
                        mLinePositions.removeLast();
                    }
                    mFromX = 0;
                    mFromY = 0;
                    mToX = 0;
                    mToY = 0;

                    invalidate();
                    return false;
                }

                mFromX = event.getX();
                mFromY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mToX = event.getX();
                mToY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mLinePositions.add(new LinePosition(
                        mFromX, mFromY, mToX, mToY));
                Log.d(TAG, "onTouch: COUNT OF LINE POS: "+ mLinePositions.size());
                break;
        }

        return true;
    }
}
