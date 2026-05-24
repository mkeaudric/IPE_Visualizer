package GeoUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class PointSet {
    List<Point> points;
    private double min_x = Double.MAX_VALUE;
    private double max_x = -Double.MAX_VALUE;
    private double min_y = Double.MAX_VALUE;
    private double max_y = -Double.MAX_VALUE;

    public PointSet() {
        this.points = new ArrayList<Point>();
    }

    public boolean addPoint(Point p) {
        if(p.x < min_x) min_x = p.x;
        if(p.x > max_x) max_x = p.x; // bukan else if, karena bisa aja min_x sama max_x nya sama saat pemasukan pertama

        if(p.y < min_y) min_y = p.y;
        if(p.y > max_y) max_y = p.y;

        return points.add(p);
    }

    // randomize titik
    public void randomizePoints(){
        Collections.shuffle(points);
    }

    // mengurutkan titik dengan menyapu secara melingkar berlawanan arah jarum jam (ccw) berdasarkan sudut kutub (titik acuan/referensi paling kiri dan terbawah) (Graham Scan Sort) */
    public void sortPoints() {
        if (points.size() < 2) return;

        // 1. Cari titik referensi (P): Y paling kecil, lalu X paling kecil
        Point p0 = points.get(0);
        for (Point p : points) {
            if (p.y < p0.y || (p.y == p0.y && p.x < p0.x)) {
                p0 = p;
            }
        }
        
        final Point ref = p0; // Titik referensi tetap untuk comparator

        // 2. Gunakan Collections.sort dengan Comparator custom
        Collections.sort(points, new Comparator<Point>() {
            @Override
            public int compare(Point b, Point c) {
                if (b == ref) return -1;
                if (c == ref) return 1;

                // Gunakan CCW untuk melihat arah dari P -> b -> c
                // Jika belok kiri, berarti b memiliki sudut lebih kecil dari c
                double res = CG.cross(ref, b, c);

                if (res > 0) return -1; // b di kiri c, b lebih dulu
                if (res < 0) return 1;  // b di kanan c, c lebih dulu
                
                // Jika kolinear, yang lebih dekat ke referensi didahulukan
                double distB = ref.distanceToOtherPoints(b);
                double distC = ref.distanceToOtherPoints(c);
                return Double.compare(distB, distC);
            }
        });
    }

    public int getSize(){
        return this.points.size();
    }

    public Point getPoint(int idx){
        return this.points.get(idx);
    }

    public double getMin_x() {
        return min_x;
    }

    public double getMax_x() {
        return max_x;
    }

    public double getMin_y() {
        return min_y;
    }

    public double getMax_y() {
        return max_y;
    }
}