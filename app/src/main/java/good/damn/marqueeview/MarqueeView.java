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
    private final Paint mPaintDebug = new Paint();

    private float mStartX = 0;
    private float mStartY = 0;

    private float mEndX = 0;
    private float mEndY = 0;

    private float mMarStartX = 0;
    private float mMarStartY = 0;
    private float mMarEndX = 1;
    private float mMarEndY = 0;

    private float mLenAngled = 5;

    private final int mLenBound = 50;

    private void init() {
        float strokeWidth = mLenBound * 0.2f;

        mPaint.setColor(0x55ffffff);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaintInteract.setColor(0xffffffff);
        mPaintInteract.setStrokeWidth(strokeWidth);
        mPaintInteract.setStrokeCap(Paint.Cap.ROUND);

        mPaintDebug.setColor(0xffffff00);

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
                mMarStartX,
                mMarStartY,
                mMarEndX,
                mMarEndY,
                mPaint);

        canvas.drawLine( // right
                mMarStartX+mLenAngled,
                mMarStartY-mLenAngled,
                mMarEndX+mLenAngled,
                mMarEndY-mLenAngled,
                mPaintDebug);

        canvas.drawLine( // left
                mMarStartX-mLenAngled,
                mMarStartY+mLenAngled,
                mMarEndX-mLenAngled,
                mMarEndY+mLenAngled,
                mPaintDebug);



        canvas.drawLine(mStartX, mStartY, mEndX, mEndY, mPaintInteract);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mMarStartX = getWidth() * 0.15f;
        mMarStartY = getHeight() * 0.15f;

        mMarEndX = getWidth() * 0.85f;
        mMarEndY = getHeight() * 0.85f;

        // Calculate angle
        float angle = (mMarEndX - mMarStartX) / (mMarEndY - mMarStartY);
        mLenAngled = mLenBound * angle;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        Log.d(TAG, "onTouch: ACTION: " + motionEvent.getAction());

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = motionEvent.getX();
                mStartY = motionEvent.getY();


                Log.d(TAG,"onTouch: TOP_LEFT: X: "+ (mMarStartX-mLenAngled) + " Y:" + (mMarStartY+mLenAngled));
                Log.d(TAG,"onTouch: TOP_RIGHT: X: " + (mMarStartX+mLenAngled) + " Y: " + (mMarStartY-mLenAngled));
                Log.d(TAG,"onTouch: BOTTOM_LEFT: X:" + (mMarEndX-mLenAngled) + " Y:" + (mMarEndY+mLenAngled));
                Log.d(TAG,"onTouch: BOTTOM_RIGHT: X: " + (mMarEndX+mLenAngled) + " Y: " + (mMarEndY-mLenAngled));

                Log.d(TAG, "onTouch: TOUCH_POS: X: " + mStartX + " Y:" + mStartY);

                // Check in-bound touch
                if (mMarStartX-mLenAngled < mStartX && mStartX < mMarStartX+mLenAngled) {

                    Log.d(TAG, "onTouch: BOUNDED");
                } else {
                    Log.d(TAG, "onTouch: NOT_IN_BOUNDED");
                    return false;
                }

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
