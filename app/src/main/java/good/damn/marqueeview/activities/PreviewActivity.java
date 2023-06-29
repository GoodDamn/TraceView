package good.damn.marqueeview.activities;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import good.damn.marqueeview.utils.FileUtils;
import good.damn.marqueeview.views.MarqueeView;

public class PreviewActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MarqueeView marqueeView = new MarqueeView(this);
        marqueeView.setBackgroundColor(0);
        marqueeView.setVectorsSource(FileUtils.retrieveSVCFile(this));

        setContentView(marqueeView);
    }
}
