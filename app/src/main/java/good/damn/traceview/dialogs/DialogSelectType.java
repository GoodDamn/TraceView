package good.damn.traceview.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.LinkedList;

import good.damn.traceview.R;
import good.damn.traceview.activities.VectorActivity;
import good.damn.traceview.graphics.editor.EntityEditor;
import good.damn.traceview.models.FileSVC;

public class DialogSelectType extends Dialog {

    private LinkedList<EntityEditor> mEntities;
    private VectorActivity mVectorActivity;

    private void init() {
        setCancelable(true);
        setContentView(R.layout.dialog_select_file_type);

        findViewById(R.id.dialog_select_type_interact)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mVectorActivity.createFileAndPreview(
                                FileSVC.TYPE_INTERACTION,
                                (byte) 0,
                                mEntities,
                                DialogSelectType.this);
                    }
                });

        findViewById(R.id.dialog_select_type_animation)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        findViewById(R.id.dialog_select_animation_options)
                                .setVisibility(View.VISIBLE);
                    }
                });

        findViewById(R.id.dialog_select_parallelAnim)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mVectorActivity.createFileAndPreview(
                                FileSVC.TYPE_ANIMATION,
                                FileSVC.ANIMATOR_PARALLEL,
                                mEntities,
                                DialogSelectType.this
                        );
                    }
                });

        findViewById(R.id.dialog_select_sequenceAnim)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mVectorActivity.createFileAndPreview(
                                FileSVC.TYPE_ANIMATION,
                                FileSVC.ANIMATOR_SEQUENCE,
                                mEntities,
                                DialogSelectType.this
                        );
                    }
                });

    }

    public DialogSelectType(@NonNull Context context) {
        super(context);
        init();
    }

    public DialogSelectType(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public void show(LinkedList<EntityEditor> e, VectorActivity a) {
        mVectorActivity = a;
        mEntities = e;
        super.show();
    }
}
