package good.damn.traceview.utils;

import android.content.Context;
import android.util.DisplayMetrics;
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
import good.damn.traceview.graphics.Entity;
import good.damn.traceview.graphics.Line;
import good.damn.traceview.graphics.editor.CircleEditor;
import good.damn.traceview.graphics.editor.EntityEditor;

public class FileUtils {

    private static final String TAG = "FileUtils";

    public static void mkSVCFile(LinkedList<EntityEditor> entities,
                                 String path,
                                 Context context) {

        FileOutputStream fos;

        try {
            File file = new File(context.getCacheDir() + path);

            if (file.createNewFile()) {
                Log.d(TAG, "mkSVCFile: SVC_FILE HAS BEEN CREATED !");
            }

            fos = new FileOutputStream(file);

            fos.write(entities.size()); // vectors size

            byte vectorType;

            for (EntityEditor entity : entities) {
                vectorType = 0; // line by default
                if (entity instanceof CircleEditor) {
                    vectorType = 1;
                }

                fos.write(vectorType);

                fos.write(ByteUtils.fixedPointNumber(entity.getStartNormalX()));
                fos.write(ByteUtils.fixedPointNumber(entity.getStartNormalY()));
                fos.write(ByteUtils.fixedPointNumber(entity.getEndNormalX()));
                fos.write(ByteUtils.fixedPointNumber(entity.getEndNormalY()));

                fos.write(ByteUtils.integer(entity.getColor()));
                fos.write(entity.getStrokeWidth());
            }

            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Entity[] retrieveSVCFile(InputStream fis, Context context)
            throws IOException {
        Entity[] entities = null;
        byte countVectors = (byte) fis.read();

        byte[] shortBuffer = new byte[2];
        byte[] intBuffer = new byte[4];

        entities = new Entity[countVectors];

        float tempX;
        float tempY;

        for (byte i = 0; i < entities.length; i++) {

            Entity entity;

            switch (fis.read()) {
                case 1:
                    entity = new Circle();
                    break;
                default:
                    entity = new Line();
                    break;
            }

            entities[i] = entity;

            fis.read(shortBuffer);
            tempX = ByteUtils.fixedPointNumber(shortBuffer);

            fis.read(shortBuffer);
            tempY = ByteUtils.fixedPointNumber(shortBuffer);

            entity.setStartNormalPoint(tempX, tempY);

            fis.read(shortBuffer);
            tempX = ByteUtils.fixedPointNumber(shortBuffer);

            fis.read(shortBuffer);
            tempY = ByteUtils.fixedPointNumber(shortBuffer);

            entity.setEndNormalPoint(tempX,tempY);

            fis.read(intBuffer);
            entity.setColor(ByteUtils.integer(intBuffer));
            Log.d(TAG, "retrieveSVCFile: COLOR: FROM: " + Arrays.toString(intBuffer) + " TO: " + entity.getColor());

            entity.setStrokeWidth((byte) fis.read());

        }

        fis.close();

        return entities;
    }

    public static Entity[] retrieveSVCFile(byte[] in, Context context) {
        try {
            return retrieveSVCFile(new ByteArrayInputStream(in), context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Entity[] retrieveSVCFile(String path, Context context) {
        try {
            return retrieveSVCFile(new FileInputStream(context.getCacheDir() + path), context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
