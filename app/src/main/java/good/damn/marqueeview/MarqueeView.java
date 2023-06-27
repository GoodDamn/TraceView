package good.damn.marqueeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Random;

public class MarqueeView extends View implements View.OnTouchListener {

    private static final String TAG = "MarqueeView";

    private final Random mRandom = new Random();

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

    //private float mLenAngled = 5;

    private final int mLenBound = 50;

    private final int mStickBound = 50;


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
        mPaintDebug.setTextSize(15.0f);

        mPaintStick.setColor(0xff00ff59);
        mPaintStick.setStrokeWidth(strokeWidth);
        mPaintStick.setStrokeCap(Paint.Cap.ROUND);

        setOnTouchListener(this);
    }

    private void randomPosition() {
        mMarStartX = getWidth() * mRandom.nextFloat();
        mMarStartY = getHeight() * mRandom.nextFloat();

        mMarEndX = getWidth() * mRandom.nextFloat();
        mMarEndY = getHeight() * mRandom.nextFloat();

        // Calculate angle
        /*mAngle = (mMarEndX - mMarStartX) / (mMarEndY - mMarStartY);
        mLenAngled = mLenBound * mAngle;*/

        mStickX = mMarStartX;
        mStickY = mMarStartY;
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

        /*canvas.drawLine( // right
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
                mPaintDebug);*/

        canvas.drawLine(mMarStartX, mMarStartY, mStickX, mStickY, mPaintStick);
        canvas.drawCircle(mStickX,mStickY, mPaintInteract.getStrokeWidth(), mPaintStick);

        float x = mStickX - mStickBound;
        float y = mStickY - mStickBound;

        canvas.drawRect(x,y,
                mStickX+mStickBound,
                mStickY+mStickBound,
                mPaintDebug);

        canvas.drawText("Y: " + y, x,y-mPaintDebug.getTextSize(),mPaintDebug);
        canvas.drawText("X: " + x, x,y-mPaintDebug.getTextSize()*2,mPaintDebug);

        //canvas.drawLine(mStartX, mStartY, mEndX, mEndY, mPaintInteract);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        randomPosition();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        float x = motionEvent.getX();
        float y = motionEvent.getY();

        Log.d(TAG, "onTouch: X: " + x + " Y: " + y);
        Log.d(TAG, "onTouch: STICK_X: " + mStickX + " STICK_Y: " + mStickY);

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (x < 50) {
                    Log.d(TAG, "onTouch: LINE WITH RANDOM POSITION");
                    randomPosition();
                    invalidate();
                    return false;
                }

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

                    float xAbs = Math.abs(mMarStartX-mMarEndX);
                    float yAbs = Math.abs(mMarStartY-mMarEndY);

                    if (yAbs < xAbs) {
                        mStickX = x;
                        mStickY = mMarStartY + (x - mMarStartX) / (mMarEndX - mMarStartX) * (mMarEndY - mMarStartY);
                    } else {
                        mStickX = mMarStartX + (y - mMarStartY) / (mMarEndY - mMarStartY) * (mMarEndX - mMarStartX);
                        mStickY = y;
                    }

                    /*// Check collision
                    if (mMarStartX < mMarEndX) {
                        if (x < mMarStartX) {
                            mStickX = mMarStartX;
                        } else if (x > mMarEndX) {
                            mStickX = mMarEndX;
                        }
                    } else {
                        if (x < mMarEndX) {
                            mStickX = mMarEndX;
                        } else if (x > mMarStartX) {
                            mStickX = mMarStartX;
                        }
                    }

                    if (mMarStartY < mMarEndY) {
                        if (y < mMarStartY) {
                            mStickY = mMarStartY;
                        } else if (y > mMarEndY) {
                            mStickY = mMarEndY;
                        }
                    } else {
                        if (y < mMarEndY) {
                            mStickY = mMarEndY;
                        } else if (y > mMarStartY) {
                            mStickY = mMarStartY;
                        }
                    }*/

                } else {
                    return false;
                }

                invalidate();
                break;
        }

        return true;
    }
}
