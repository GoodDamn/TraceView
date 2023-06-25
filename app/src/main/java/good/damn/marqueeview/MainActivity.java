package good.damn.marqueeview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MarqueeView marqueeView = new MarqueeView(this);
        marqueeView.setBackgroundColor(0);

        setContentView(marqueeView);
    }
}