package good.damn.marqueeview.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import good.damn.marqueeview.views.MarqueeEditorView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MarqueeEditorView marqueeView = new MarqueeEditorView(this);
        marqueeView.setBackgroundColor(0);

        setContentView(marqueeView);
    }
}