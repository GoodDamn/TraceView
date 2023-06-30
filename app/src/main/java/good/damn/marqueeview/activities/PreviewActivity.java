package good.damn.marqueeview.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import good.damn.marqueeview.interfaces.OnMarqueeFinishListener;
import good.damn.marqueeview.utils.FileUtils;
import good.damn.marqueeview.views.MarqueeView;

public class PreviewActivity extends AppCompatActivity {

    private static final String TAG = "PreviewActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MarqueeView marqueeView = new MarqueeView(this);
        marqueeView.setBackgroundColor(0);
        marqueeView.setVectorsSource(FileUtils.retrieveSVCFile(this));

        marqueeView.setOnMarqueeFinishListener(new OnMarqueeFinishListener() {
            @Override
            public void onFinish() {
                marqueeView.restart();
                Toast.makeText(PreviewActivity.this,
                        "MARQUEE FINISHED!",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        setContentView(marqueeView);
    }
}
