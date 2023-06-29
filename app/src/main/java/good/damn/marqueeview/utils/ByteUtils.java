package good.damn.marqueeview.utils;

import android.util.Log;

public class ByteUtils {

    private static final String TAG = "ByteUtils";

    public static byte[] fixedPointNumber(float val) {
        short v = (short) (val * 1000f);
        Log.d(TAG, "fixedPointNumber: FROM: " + val + " TO: " + v);
        return new byte[] {
                (byte) ((v >> 8) & 0xff),
                (byte) (v & 0xff)
        };
    }

    public static float fixedPointNumber(byte[] in) {
        short v = (short) ((in[0] & 0xff) << 8 | (in[1] & 0xff));
        float n = v / 1000f;
        Log.d(TAG, "fixedPointNumber: IN_BYTES: FROM: " + v + " TO: " + n);
        return n;
    }

    public static byte[] integer(int val) {
        return new byte[] {
                (byte) ((val >> 24) & 0xff),
                (byte) ((val >> 16) & 0xff),
                (byte) ((val >> 8) & 0xff),
                (byte) (val & 0xff)
        };
    }
}