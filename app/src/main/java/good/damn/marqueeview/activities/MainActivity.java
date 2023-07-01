package good.damn.marqueeview.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;

import good.damn.gradient_color_picker.GradientColorPicker;
import good.damn.gradient_color_picker.OnPickColorListener;
import good.damn.marqueeview.views.MarqueeEditorView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MarqueeEditorView editorView = new MarqueeEditorView(this);
        editorView.setBackgroundColor(0);

        GradientColorPicker colorPicker = new GradientColorPicker(this);
        colorPicker.setOnPickColorListener(new OnPickColorListener() {
            @Override
            public void onPickColor(int color) {
                editorView.setLineColor(color);
            }
        });

        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(320,150);
        param.gravity = Gravity.BOTTOM;

        colorPicker.setLayoutParams(param);

        FrameLayout frameLayout = new FrameLayout(this);

        frameLayout.addView(editorView,
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        frameLayout.addView(colorPicker);

        setContentView(frameLayout);
    }
}