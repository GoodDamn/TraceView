package good.damn.traceview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import java.util.LinkedList;

import good.damn.traceview.graphics.editor.CircleEditor;
import good.damn.traceview.graphics.editor.EntityEditor;
import good.damn.traceview.graphics.editor.LineEditor;

public class TraceEditorView extends View implements View.OnTouchListener {

    private static final String TAG = "MarqueeEditorView";

    private final Paint mPaintBackground = new Paint();
    private final Paint mPaintForeground = new Paint();

    private final LinkedList<EntityEditor> mEntities = new LinkedList<>();

    private EntityEditor mEntity;

    private OnClickIconListener mOnStartClickListener;

    private float mFromX;
    private float mFromY;

    private float mToX;
    private float mToY;

    private float mMinStrokeWidthY = 1;
    private float mMaxStrokeWidthY = 127;
    private float mCurrentStrokeWidthY = 0;

    private boolean mDoesStrokeEdit = false;

    private void init() {

        mPaintBackground.setColor(0x55ffffff);
        mPaintBackground.setStrokeWidth(15.0f);
        mPaintBackground.setStrokeCap(Paint.Cap.ROUND);

        mPaintForeground.setColor(0xff00ff59);

        mEntity = new LineEditor(mPaintForeground, mPaintBackground);

        setOnTouchListener(this);
    }

    public TraceEditorView(Context context) {
        super(context);
        init();
    }

    public TraceEditorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TraceEditorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnStartClickListener(OnClickIconListener onClickListener) {
        mOnStartClickListener = onClickListener;
    }

    public void setLineColor(@ColorInt int color) {
        mPaintForeground.setColor(color);
        mEntity.setColor(color);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int mid = getHeight() >> 1;
        mMinStrokeWidthY = mid - 200;
        mMaxStrokeWidthY = 200 + mid;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(0,mMinStrokeWidthY, 50, mMaxStrokeWidthY, mPaintForeground);
        canvas.drawLine(50,mMaxStrokeWidthY, 0, mMaxStrokeWidthY, mPaintForeground);

        canvas.drawCircle(0,mCurrentStrokeWidthY, 15, mPaintForeground);

        for (EntityEditor entity: mEntities) { // already places entities
            float sX = entity.getStartNormalX() * getWidth();
            float sY = entity.getStartNormalY() * getHeight();

            entity.draw(canvas, sX, sY,
                    entity.getEndNormalX() * getWidth(),
                    entity.getEndNormalY() * getHeight());
        }

        // For new placing entity
        mEntity.draw(canvas,mFromX,mFromY, mToX, mToY);

        // Draw icons:
        // Triangle
        canvas.drawLine(25,25,25,75, mPaintForeground);
        canvas.drawLine(25,75,75,50, mPaintForeground);
        canvas.drawLine(75,50,25,25, mPaintForeground);

        // Circle
        canvas.drawCircle(150,50,25, mPaintForeground);

        // Line
        canvas.drawLine(225, 75,275,25, mPaintBackground);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (event.getX() > getWidth() - 100 && event.getY() < 100) { // Undo previous action
                    if (mEntities.size() != 0) {
                        mEntities.removeLast();
                    }
                    mFromX = 0;
                    mFromY = 0;
                    mToX = 0;
                    mToY = 0;

                    invalidate();
                    return false;
                }

                if (event.getX() > 100 && event.getX() < 200 && event.getY() < 100) { // draw circles
                    mEntity = new CircleEditor(mPaintForeground, mPaintBackground);
                    return false;
                }

                if (event.getX() > 200 && event.getX() < 300 && event.getY() < 100) { // draw Line
                    mEntity = new LineEditor(mPaintForeground, mPaintBackground);
                    return false;
                }

                if (event.getX() < 100 && event.getY() < 100) { // start preview mode
                    mOnStartClickListener.onClick(mEntities);
                    return false;
                }

                if (event.getX() < 50
                    && event.getY() > mMinStrokeWidthY
                    && event.getY() < mMaxStrokeWidthY) {
                    mDoesStrokeEdit = true;
                    return true;
                }

                mFromX = event.getX();
                mFromY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDoesStrokeEdit) {
                    float cur = (event.getY() - mMinStrokeWidthY) / (mMaxStrokeWidthY-mMinStrokeWidthY);
                    mCurrentStrokeWidthY = mMinStrokeWidthY + cur * 400;
                    mEntity.setStrokeWidth((byte) (cur * 127));
                    Log.d(TAG, "onTouch: STROKE_EDIT: " + mEntity.getStrokeWidth());
                    invalidate();
                    break;
                }

                mToX = event.getX();
                mToY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (mDoesStrokeEdit) {
                    mDoesStrokeEdit = false;
                    break;
                }

                EntityEditor entityNew = mEntity.copy();

                entityNew.setStartNormalPoint(
                        mFromX / getWidth(),
                        mFromY / getHeight());

                entityNew.setEndNormalPoint(
                        mToX / getWidth(),
                        mToY / getHeight());

                mEntities.add(entityNew);
                Log.d(TAG, "onTouch: COUNT OF LINE POS: "+ mEntities.size());
                break;
        }

        return true;
    }

    public interface OnClickIconListener {
        void onClick(LinkedList<EntityEditor> entities);
    }
}
