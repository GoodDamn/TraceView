package good.damn.traceview.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.LinkedList;

import good.damn.traceview.R;
import good.damn.traceview.dialogs.DialogSelectType;
import good.damn.traceview.fragments.PreviewFragment;
import good.damn.traceview.fragments.VectorEditorFragment;
import good.damn.traceview.graphics.Entity;
import good.damn.traceview.graphics.editor.EntityEditor;
import good.damn.traceview.models.FileSVC;
import good.damn.traceview.utils.FileUtils;
import good.damn.traceview.views.BlockedViewPager;
import good.damn.traceview.views.TraceEditorView;

public class VectorActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private int moveToPos;

    private BlockedViewPager mViewPager;
    final Runnable mPagerRunnable = () -> mViewPager.setCurrentItem(moveToPos);

    private PreviewFragment mPreviewFragment;

    private DialogSelectType mDialogType;

    public void createFileAndPreview(
            byte type,
            byte animator,
            LinkedList<EntityEditor> entities,
            Dialog dialog) {

        String path = "/dumb.svc";

        FileUtils.mkSVCFile(entities,
                type,
                path,
                animator,
                VectorActivity.this);
        moveToPos = 1;
        mViewPager.post(mPagerRunnable);
        mPreviewFragment.startPreview(path);
        dialog.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new BlockedViewPager(this);
        mViewPager.setId(ViewCompat.generateViewId());

        VectorEditorFragment editorFragment = new VectorEditorFragment();
        mPreviewFragment = new PreviewFragment();

        mDialogType = new DialogSelectType(this);

        editorFragment.setOnStartClickListener(new TraceEditorView.OnClickIconListener() {
            @Override
            public void onClick(LinkedList<EntityEditor> entities) {
                mDialogType.show(entities, VectorActivity.this);
            }
        });

        final Fragment[] fragments = new Fragment[]{
                editorFragment,
                mPreviewFragment
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