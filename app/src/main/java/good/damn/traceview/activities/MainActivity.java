package good.damn.traceview.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import good.damn.traceview.fragments.PreviewFragment;
import good.damn.traceview.fragments.VectorEditorFragment;
import good.damn.traceview.views.BlockedViewPager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private int moveToPos = 0;

    private BlockedViewPager mViewPager;
    final Runnable mPagerRunnable = () -> mViewPager.setCurrentItem(moveToPos);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new BlockedViewPager(this);
        mViewPager.setId(ViewCompat.generateViewId());

        VectorEditorFragment editorFragment = new VectorEditorFragment();
        editorFragment.setOnStartClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: CURRENT_ITEM: ");
                moveToPos = 1;
                mViewPager.post(mPagerRunnable);
            }
        });

        final Fragment[] fragments = new Fragment[]{
                editorFragment,
                new PreviewFragment()
        };

        mViewPager.setOffscreenPageLimit(2);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Log.d(TAG, "getItem: POS: " + position);
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });

        setContentView(mViewPager);
    }

    @Override
    public void onBackPressed() {

        if (mViewPager.getCurrentItem() == 0) {
            super.onBackPressed();
            return;
        }

        moveToPos = 0;
        mViewPager.post(mPagerRunnable);

    }
}