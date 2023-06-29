package good.damn.marqueeview.models;

public class LineConfig {
    public float fromX;
    public float fromY;

    public float toX;
    public float toY;

    public int color = 0xffff0000;
    public float strokeWidth = 10.0f;

    public LineConfig() {}

    public LineConfig(float fx, float fy, float tx, float ty) {
        fromX = fx;
        fromY = fy;
        toX = tx;
        toY = ty;
    }
}
