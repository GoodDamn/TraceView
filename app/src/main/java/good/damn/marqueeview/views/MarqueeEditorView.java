package good.damn.marqueeview.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.Random;

import good.damn.marqueeview.activities.PreviewActivity;
import good.damn.marqueeview.graphics.editor.CircleEditor;
import good.damn.marqueeview.graphics.editor.EntityEditor;
import good.damn.marqueeview.graphics.editor.LineEditor;
import good.damn.marqueeview.models.EditorConfig;
import good.damn.marqueeview.utils.FileUtils;

public class MarqueeEditorView extends View implements View.OnTouchListener {

    private static final String TAG = "MarqueeEditorView";
    private static final Random mRandom = new Random();

    private final Paint mPaint = new Paint();
    private final Paint mPaintCircle = new Paint();

    private final LinkedList<EditorConfig> mEditorConfigs = new LinkedList<>();

    private float mFromX;
    private float mFromY;

    private float mToX;
    private float mToY;

    private EntityEditor mEntityEditor;

    private void init() {

        mPaint.setColor(0x55ffffff);
        mPaint.setStrokeWidth(15.0f);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaintCircle.setColor(0xff00ff59);

        mEntityEditor = new LineEditor(mPaintCircle,mPaint);

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

    public void setLineColor(@ColorInt int color) {
        mEntityEditor.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (EditorConfig config: mEditorConfigs) { // already places entities
            float sX = config.fromX*getWidth();
            float sY = config.fromY*getHeight();

            config.entityEditor.draw(canvas, sX, sY, config.toX * getWidth(), config.toY*getHeight());
        }

        // For new placing entity
        mEntityEditor.draw(canvas,mFromX,mFromY, mToX, mToY);

        // Draw icons:
        // Triangle
        canvas.drawLine(25,25,25,75,mPaint);
        canvas.drawLine(25,75,75,50,mPaint);
        canvas.drawLine(75,50,25,25,mPaint);

        // Circle
        canvas.drawCircle(150,50,25,mPaint);

        // Line
        canvas.drawLine(225, 75,275,25,mPaint);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (event.getX() > getWidth() - 100 && event.getY() < 100) { // Undo previous action
                    if (mEditorConfigs.size() != 0) {
                        mEditorConfigs.removeLast();
                    }
                    mFromX = 0;
                    mFromY = 0;
                    mToX = 0;
                    mToY = 0;

                    invalidate();
                    return false;
                }

                if (event.getX() > 100 && event.getX() < 200 && event.getY() < 100) { // draw circles
                    mEntityEditor = new CircleEditor(mPaintCircle, mPaint);
                    return false;
                }

                if (event.getX() > 200 && event.getX() < 300 && event.getY() < 100) { // draw Line
                    mEntityEditor = new LineEditor(mPaintCircle, mPaint);
                    return false;
                }

                if (event.getX() < 100 && event.getY() < 100) { // start preview mode

                    FileUtils.mkSVCFile(getContext(), mEditorConfigs);
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
                EditorConfig config = new EditorConfig(
                        mFromX / getWidth(),
                        mFromY / getHeight(),
                        mToX / getWidth(),
                        mToY / getHeight());

                config.entityEditor = mEntityEditor.copy();
                config.color = mEntityEditor.getColor();
                mEditorConfigs.add(config);
                Log.d(TAG, "onTouch: COUNT OF LINE POS: "+ mEditorConfigs.size());
                break;
        }

        return true;
    }
}
