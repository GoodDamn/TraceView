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
import good.damn.traceview.utils.FileUtils;
import good.damn.traceview.utils.models.EditorConfig;

public class TraceEditorView extends View implements View.OnTouchListener {

    private static final String TAG = "MarqueeEditorView";

    private final Paint mPaintBackground = new Paint();
    private final Paint mPaintForeground = new Paint();

    private final LinkedList<EditorConfig> mEditorConfigs = new LinkedList<>();

    private float mFromX;
    private float mFromY;

    private float mToX;
    private float mToY;

    private EntityEditor mEntityEditor;

    private OnClickIconListener mOnStartClickListener;

    private void init() {

        mPaintBackground.setColor(0x55ffffff);
        mPaintBackground.setStrokeWidth(15.0f);
        mPaintBackground.setStrokeCap(Paint.Cap.ROUND);

        mPaintForeground.setColor(0xff00ff59);

        mEntityEditor = new LineEditor(mPaintForeground, mPaintBackground);

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
                    mEntityEditor = new CircleEditor(mPaintForeground, mPaintBackground);
                    return false;
                }

                if (event.getX() > 200 && event.getX() < 300 && event.getY() < 100) { // draw Line
                    mEntityEditor = new LineEditor(mPaintForeground, mPaintBackground);
                    return false;
                }

                if (event.getX() < 100 && event.getY() < 100) { // start preview mode
                    mOnStartClickListener.onClick(mEditorConfigs);
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
                config.entityEditor.setColor(mEntityEditor.getColor());
                mEditorConfigs.add(config);
                Log.d(TAG, "onTouch: COUNT OF LINE POS: "+ mEditorConfigs.size());
                break;
        }

        return true;
    }

    public interface OnClickIconListener {
        void onClick(LinkedList<EditorConfig> editorConfigs);
    }
}
