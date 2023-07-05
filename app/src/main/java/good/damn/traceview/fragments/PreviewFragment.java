package good.damn.traceview.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import good.damn.traceview.interfaces.OnTraceFinishListener;
import good.damn.traceview.utils.FileUtils;
import good.damn.traceview.views.TraceView;

public class PreviewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Context context = getContext();

        TraceView traceView = new TraceView(context);
        traceView.setBackgroundColor(0);
        traceView.setVectorsSource(FileUtils.retrieveSVCFile(context));

        traceView.setOnTraceFinishListener(new OnTraceFinishListener() {
            @Override
            public void onFinish() {
                traceView.restart();
                Toast.makeText(context,
                                "MARQUEE FINISHED!",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });
        return traceView;
    }
}
