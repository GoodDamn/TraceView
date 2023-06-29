package good.damn.marqueeview.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.Random;

import good.damn.marqueeview.activities.PreviewActivity;
import good.damn.marqueeview.models.LineConfig;
import good.damn.marqueeview.utils.FileUtils;

public class MarqueeEditorView extends View implements View.OnTouchListener {

    private static final String TAG = "MarqueeEditorView";
    private static final Random mRandom = new Random();

    private final Paint mPaint = new Paint();
    private final Paint mPaintCircle = new Paint();

    private final LinkedList<LineConfig> mLineConfigs = new LinkedList<>();

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

        for (LineConfig position: mLineConfigs) {
            float sX = position.fromX*getWidth();
            float sY = position.fromY*getHeight();
            canvas.drawLine(sX, sY, position.toX * getWidth(), position.toY*getHeight(), mPaint);
            canvas.drawCircle(sX, sY, mPaint.getStrokeWidth(),mPaintCircle);
        }

        canvas.drawLine(mFromX, mFromY, mToX, mToY, mPaint);
        canvas.drawCircle(mFromX, mFromY, mPaint.getStrokeWidth(),mPaintCircle);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (event.getX() > getWidth() - 100 && event.getY() < 100) { // Undo previous action
                    if (mLineConfigs.size() != 0) {
                        mLineConfigs.removeLast();
                    }
                    mFromX = 0;
                    mFromY = 0;
                    mToX = 0;
                    mToY = 0;

                    invalidate();
                    return false;
                }

                if (event.getX() < 100 && event.getY() < 100) { // start preview mode

                    FileUtils.mkSVCFile(getContext(), mLineConfigs);
                    Intent intent = new Intent(getContext(), PreviewActivity.class);
                    getContext().startActivity(intent);

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
                LineConfig config = new LineConfig(
                        mFromX / getWidth(),
                        mFromY / getHeight(),
                        mToX / getWidth(),
                        mToY / getHeight());
                config.color =
                        (0xFF << 24) |
                        ((mRandom.nextInt(205)+50) << 16) |
                        ((mRandom.nextInt(205)+50) << 8) |
                        (mRandom.nextInt(205)+50);
                mLineConfigs.add(config);
                Log.d(TAG, "onTouch: COUNT OF LINE POS: "+ mLineConfigs.size());
                break;
        }

        return true;
    }
}
