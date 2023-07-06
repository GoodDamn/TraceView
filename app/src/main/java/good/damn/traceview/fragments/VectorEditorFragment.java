package good.damn.traceview.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import good.damn.gradient_color_picker.GradientColorPicker;
import good.damn.gradient_color_picker.OnPickColorListener;
import good.damn.traceview.views.TraceEditorView;

public class VectorEditorFragment extends Fragment {

    private TraceEditorView.OnClickIconListener mOnStartClickListener;

    public void setOnStartClickListener(TraceEditorView.OnClickIconListener onClickListener) {
        mOnStartClickListener = onClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Context context = getContext();

        TraceEditorView editorView = new TraceEditorView(context);
        editorView.setBackgroundColor(0);

        GradientColorPicker colorPicker = new GradientColorPicker(context);
        colorPicker.setOnPickColorListener(new OnPickColorListener() {
            @Override
            public void onPickColor(int color) {
                editorView.setLineColor(color);
            }
        });

        editorView.setOnStartClickListener(mOnStartClickListener);

        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(320,150);
        param.gravity = Gravity.BOTTOM;

        colorPicker.setLayoutParams(param);

        FrameLayout frameLayout = new FrameLayout(context);

        frameLayout.addView(editorView,
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        frameLayout.addView(colorPicker);
        return frameLayout;
    }
}
