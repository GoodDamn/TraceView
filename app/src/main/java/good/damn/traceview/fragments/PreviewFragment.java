package good.damn.traceview.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import good.damn.traceview.interfaces.OnTraceFinishListener;
import good.damn.traceview.models.FileSVC;
import good.damn.traceview.utils.FileUtils;
import good.damn.traceview.views.TraceView;

public class PreviewFragment extends Fragment {

    private static final String TAG = "PreviewFragment";

    private TraceView mTraceView;

    public void startPreview(String path) {
        FileSVC fileSVC = FileUtils.retrieveSVCFile(path,getContext());
        if (fileSVC == null) {
            Log.d(TAG, "startPreview: FILE.svc IS NULL");
            return;
        }

        mTraceView.setVectorsSource(fileSVC);
        mTraceView.startAnimation();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Context context = getContext();

        mTraceView = new TraceView(context);
        mTraceView.setBackgroundColor(0);

        mTraceView.setOnTraceFinishListener(new OnTraceFinishListener() {
            @Override
            public void onFinish() {
                mTraceView.restart();
                Toast.makeText(context,
                                "MARQUEE FINISHED!",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        return mTraceView;
    }
}
