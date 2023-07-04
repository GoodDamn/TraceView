package good.damn.traceview.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import good.damn.traceview.interfaces.OnMarqueeFinishListener;
import good.damn.traceview.utils.FileUtils;
import good.damn.traceview.views.TraceView;

public class PreviewActivity extends AppCompatActivity {

    private static final String TAG = "PreviewActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TraceView traceView = new TraceView(this);
        traceView.setBackgroundColor(0);
        traceView.setVectorsSource(FileUtils.retrieveSVCFile(this));

        traceView.setOnMarqueeFinishListener(new OnMarqueeFinishListener() {
            @Override
            public void onFinish() {
                traceView.restart();
                Toast.makeText(PreviewActivity.this,
                        "MARQUEE FINISHED!",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        setContentView(traceView);
    }
}
