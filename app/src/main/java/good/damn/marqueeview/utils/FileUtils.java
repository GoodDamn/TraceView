package good.damn.marqueeview.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import good.damn.marqueeview.models.LineConfig;

public class FileUtils {

    private static final String TAG = "FileUtils";

    public static void mkSVCFile(Context context, LinkedList<LineConfig> lineConfigs) {

        FileOutputStream fos;


        try {
            File file = new File(context.getCacheDir()+"/dumb.svc");

            if (file.createNewFile()) {
                Log.d(TAG, "mkSVCFile: SVC_FILE HAS BEEN CREATED !");
            }

            fos = new FileOutputStream(file);

            fos.write(lineConfigs.size()); // vectors size

            for (LineConfig l: lineConfigs) {
                fos.write(0);
                fos.write(ByteUtils.fixedPointNumber(l.fromX));
                fos.write(ByteUtils.fixedPointNumber(l.fromY));
                fos.write(ByteUtils.fixedPointNumber(l.toX));
                fos.write(ByteUtils.fixedPointNumber(l.toY));
                fos.write(ByteUtils.integer(l.color));
                fos.write((byte) l.strokeWidth);
            }

            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LineConfig[] retrieveSVCFile(Context context) {
        LineConfig[] lineConfigs = null;
        try {
            FileInputStream fis = new FileInputStream(context.getCacheDir()+"/dumb.svc");

            byte countVectors = (byte) fis.read();

            byte[] shortBuffer = new byte[2];
            byte[] intBuffer = new byte[4];

            lineConfigs = new LineConfig[countVectors];

            for (byte i = 0; i < lineConfigs.length; i++) {
                lineConfigs[i] = new LineConfig();
                LineConfig c = lineConfigs[i];

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

        return lineConfigs;
    }
}
