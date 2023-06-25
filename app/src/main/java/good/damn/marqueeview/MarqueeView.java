package good.damn.marqueeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class MarqueeView extends View implements View.OnTouchListener {

    private static final String TAG = "MarqueeView";

    private final Paint mPaint = new Paint();
    private final Paint mPaintInteract = new Paint();

    private float mStartX = 0;
    private float mStartY = 0;

    private float mEndX = 0;
    private float mEndY = 0;

    private void init() {
        mPaint.setColor(0x55ffffff);
        mPaint.setStrokeWidth(10);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaintInteract.setColor(0xffffffff);
        mPaintInteract.setStrokeWidth(10);
        mPaintInteract.setStrokeCap(Paint.Cap.ROUND);

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

        // Background line
        canvas.drawLine(
                getWidth() * 0.15f,
                    getHeight() * 0.15f,
                getWidth()*0.85f,
                    getHeight()*0.85f,
                mPaint);

        canvas.drawLine(mStartX, mStartY, mEndX, mEndY, mPaintInteract);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        Log.d(TAG, "onTouch: ACTION: " + motionEvent.getAction());

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = motionEvent.getX();
                mStartY = motionEvent.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mEndX = motionEvent.getX();
                mEndY = motionEvent.getY();
                invalidate();
                break;
        }

        return true;
    }
}
