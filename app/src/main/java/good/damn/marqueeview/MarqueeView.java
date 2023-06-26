package good.damn.marqueeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
    private final Paint mPaintStick = new Paint();

    private float mMarStartX = 0;
    private float mMarStartY = 0;
    private float mMarEndX = 1;
    private float mMarEndY = 0;

    private float mStickX = 0;
    private float mStickY = 0;

    private float mAngle = 0;

    private float mLenAngled = 5;

    private final int mStickBound = 50;

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
        mPaintDebug.setStyle(Paint.Style.STROKE);

        mPaintStick.setColor(0xff00ff59);
        mPaintStick.setStrokeWidth(strokeWidth);
        mPaintStick.setStrokeCap(Paint.Cap.ROUND);

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

        canvas.drawLine(mMarStartX, mMarStartY, mStickX, mStickY, mPaintStick);
        canvas.drawCircle(mStickX,mStickY, mPaintInteract.getStrokeWidth(), mPaintStick);

        canvas.drawRect(mStickX-mStickBound,
                mStickY-mStickBound,
                mStickX+mStickBound,
                mStickY+mStickBound,
                mPaintDebug);

        //canvas.drawLine(mStartX, mStartY, mEndX, mEndY, mPaintInteract);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mMarStartX = getWidth() * 0.15f;
        mMarStartY = getHeight() * 0.15f;

        mMarEndX = getWidth() * 0.85f;
        mMarEndY = getHeight() * 0.85f;

        // Calculate angle
        mAngle = (mMarEndX - mMarStartX) / (mMarEndY - mMarStartY);
        mLenAngled = mLenBound * mAngle;

        mStickX = mMarStartX;
        mStickY = mMarStartY;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        float x = motionEvent.getX();
        float y = motionEvent.getY();

        Log.d(TAG, "onTouch: X: " + x + " Y: " + y);
        Log.d(TAG, "onTouch: STICK_X: " + mStickX + " STICK_Y: " + mStickY);

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (mStickX-mStickBound < x && x < mStickX+mStickBound &&
                    mStickY-mStickBound < y && y < mStickY+mStickBound) {
                    Log.d(TAG, "onTouch: INSIDE_STICK_BOUND");
                } else {
                    return false;
                }

                break;
            case MotionEvent.ACTION_MOVE:

                if (mStickX-mStickBound < x && x < mStickX+mStickBound &&
                    mStickY-mStickBound < y && y < mStickY+mStickBound) {

                    mStickX = x;
                    mStickY = mMarStartY + (x - mMarStartX) / (mMarEndX - mMarStartX) * (mMarEndY-mMarStartY);

                    if (x < mMarStartX) {
                        mStickX = mMarStartX;
                        mStickY = mMarStartY;
                    } else if (x > mMarEndX) {
                        mStickX = mMarEndX;
                        mStickY = mMarEndY;
                    }
                } else {
                    return false;
                }

                invalidate();
                break;
        }

        return true;
    }
}
