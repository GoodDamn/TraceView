package good.damn.traceview.utils;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;

import good.damn.traceview.graphics.Circle;
import good.damn.traceview.graphics.Line;
import good.damn.traceview.graphics.editor.CircleEditor;
import good.damn.traceview.utils.models.EditorConfig;
import good.damn.traceview.utils.models.EntityConfig;

public class FileUtils {

    private static final String TAG = "FileUtils";

    public static void mkSVCFile(LinkedList<EditorConfig> entityConfigs,
                                 String path,
                                 Context context) {

        FileOutputStream fos;

        try {
            File file = new File(context.getCacheDir() + path);

            if (file.createNewFile()) {
                Log.d(TAG, "mkSVCFile: SVC_FILE HAS BEEN CREATED !");
            }

            fos = new FileOutputStream(file);

            fos.write(entityConfigs.size()); // vectors size

            byte vectorType;

            for (EditorConfig l : entityConfigs) {
                vectorType = 0; // line by default
                if (l.entityEditor instanceof CircleEditor) {
                    vectorType = 1;
                }
                fos.write(vectorType);
                fos.write(ByteUtils.fixedPointNumber(l.fromX));
                fos.write(ByteUtils.fixedPointNumber(l.fromY));
                fos.write(ByteUtils.fixedPointNumber(l.toX));
                fos.write(ByteUtils.fixedPointNumber(l.toY));
                fos.write(ByteUtils.integer(l.entityEditor.getColor()));
                fos.write(l.entityEditor.getStrokeWidth());
            }

            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static EntityConfig[] retrieveSVCFile(InputStream fis, Context context)
            throws IOException {
        EntityConfig[] entityConfigs = null;
        byte countVectors = (byte) fis.read();

        byte[] shortBuffer = new byte[2];
        byte[] intBuffer = new byte[4];

        entityConfigs = new EntityConfig[countVectors];

        for (byte i = 0; i < entityConfigs.length; i++) {
            entityConfigs[i] = new EntityConfig();
            EntityConfig c = entityConfigs[i];

            byte vectorType = (byte) fis.read();

            fis.read(shortBuffer);
            c.fromX = ByteUtils.fixedPointNumber(shortBuffer);

            fis.read(shortBuffer);
            c.fromY = ByteUtils.fixedPointNumber(shortBuffer);

            fis.read(shortBuffer);
            c.toX = ByteUtils.fixedPointNumber(shortBuffer);

            fis.read(shortBuffer);
            c.toY = ByteUtils.fixedPointNumber(shortBuffer);

            switch (vectorType) {
                case 0:
                    c.entity = new Line();
                    break;
                case 1:
                    c.entity = new Circle();
                    break;
            }

            fis.read(intBuffer);
            c.entity.setColor(ByteUtils.integer(intBuffer));
            Log.d(TAG, "retrieveSVCFile: COLOR: FROM: " + Arrays.toString(intBuffer) + " TO: " + c.entity.getColor());

            c.entity.setStrokeWidth((byte) fis.read());

        }

        fis.close();

        return entityConfigs;
    }

    public static EntityConfig[] retrieveSVCFile(byte[] in, Context context) {
        try {
            return retrieveSVCFile(new ByteArrayInputStream(in), context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static EntityConfig[] retrieveSVCFile(String path, Context context) {
        try {
            return retrieveSVCFile(new FileInputStream(context.getCacheDir() + path), context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
