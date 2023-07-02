package good.damn.marqueeview.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import good.damn.marqueeview.models.EntityConfig;

public class FileUtils {

    private static final String TAG = "FileUtils";

    public static void mkSVCFile(Context context, LinkedList<EntityConfig> entityConfigs) {

        FileOutputStream fos;


        try {
            File file = new File(context.getCacheDir()+"/dumb.svc");

            if (file.createNewFile()) {
                Log.d(TAG, "mkSVCFile: SVC_FILE HAS BEEN CREATED !");
            }

            fos = new FileOutputStream(file);

            fos.write(entityConfigs.size()); // vectors size

            for (EntityConfig l: entityConfigs) {
                fos.write(0);
                fos.write(ByteUtils.fixedPointNumber(l.fromX));
                fos.write(ByteUtils.fixedPointNumber(l.fromY));
                fos.write(ByteUtils.fixedPointNumber(l.toX));
                fos.write(ByteUtils.fixedPointNumber(l.toY));
                fos.write(ByteUtils.integer(l.color));
                fos.write(l.strokeWidth);
            }

            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static EntityConfig[] retrieveSVCFile(Context context) {
        EntityConfig[] entityConfigs = null;
        try {
            FileInputStream fis = new FileInputStream(context.getCacheDir()+"/dumb.svc");

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

                fis.read(intBuffer);
                c.color = ByteUtils.integer(intBuffer);
                Log.d(TAG, "retrieveSVCFile: COLOR: FROM: " + Arrays.toString(intBuffer) + " TO: " + c.color);

                c.strokeWidth = (byte) fis.read();
            }

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return entityConfigs;
    }
}
