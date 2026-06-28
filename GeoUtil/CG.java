package GeoUtil;
// kelas utilitas
public class CG {
    public static final double EPS = 0.00000001;

    // > 0 -> belok kiri
    // < 0 -> belok kanan
    // = 0 -> lurus

    public static double dot(Point p, Point q, Point r) {
        double res = (q.x - p.x)*(r.x - p.x) + (q.y - p.y)*(r.y - p.y);
        return Math.abs(res) <= EPS ? 0 : res;
    }

    public static double cross(Point p, Point q, Point r) {
        double res = (q.x - p.x)*(r.y - q.y) - (q.y - p.y)*(r.x - q.x);
        return Math.abs(res) <= EPS ? 0 : res;
    }
}
