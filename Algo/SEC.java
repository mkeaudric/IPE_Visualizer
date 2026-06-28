package Algo;
import GeoUtil.Circle;
import GeoUtil.Point;
import GeoUtil.PointSet;
import XMLUtil.AppendIpeData;

public class SEC {
    private int step = 1; 
    private StringBuilder newLayer = new StringBuilder();
    private StringBuilder data = new StringBuilder();
    AppendIpeData appender = new AppendIpeData();

    public Circle miniDisc(PointSet P){
        // permutasi random P
        P.randomizePoints();
        data.append(appender.pointSetToIpe(P, "black", "titik_dasar"));

        // loop
        int i, len = P.getSize();

        // kalo titik < 2
        if(len == 0) return null;
        if(len == 1) {
            Circle C = new Circle(P.getPoint(0), P.getPoint(0));
            data.append(appender.recordStep(C, "step_" + step++, "red", "Selesai: Hanya ada 1 titik."));
            return C;
        }

        Point p = P.getPoint(0);
        Point p1 = P.getPoint(1);
        Circle C = new Circle(p, p1);

        data.append(appender.recordStep(C, "step_" + step++, "blue", 
            "Inisialisasi basis awal dari titik $x_0$ dan $x_1$."
        ));

        Point pi;
        for(i = 2; i < len; i++){
            pi = P.getPoint(i);
            String desc;
            
            if(!C.contains(pi)){
                // Tambahkan argumen indeks 'i' ke parameter fungsi
                C = miniDiscWith1Point(P, i, pi, i);
                desc = String.format("Evaluasi $x_{%d}$: Titik berada di \\textbf{luar}. Lingkaran di-*update* dengan $x_{%d}$ sebagai batas.", i, i);
            } else {
                desc = String.format("Evaluasi $x_{%d}$: Titik berada di \\textbf{dalam} lingkaran. Status aman.", i);
            }
            data.append(appender.recordStep(C, "step_" + step++, "blue", desc));
        }

        data.append(appender.recordStep(C, "step_" + step, "red", "{\\Large \\bf \\color{red} Algoritma Selesai!}"));

        return C;
    }

    public Circle miniDiscWith1Point(PointSet P, int n, Point q, int qIdx){
        if(n == 0) {
            Circle C = new Circle(q, q);
            data.append(appender.recordStep(C, "step_" + step++, "blue", "Rekursi 1-Batas: Lingkaran dari 1 titik (" + qIdx + ")."));
            return C;
        }

        Point p = P.getPoint(0);
        Point center = circleCenter2Points(p, q);
        Circle C = new Circle(center, q);

        data.append(appender.recordStep(C, "step_" + step++, "blue", 
            String.format("Mulai Rekursi (Batas = $x_{%d}$): Buat lingkaran awal pakai $x_0$ dan $x_{%d}$.", qIdx, qIdx)
        ));

        int i;
        Point pi;
        for(i = 1; i < n; i++){
            pi = P.getPoint(i);
            String desc;
            if(!C.contains(pi)){
                // Lempar qIdx dan indeks i ke rekursi selanjutnya
                C = miniDiscWith2Points(P, i, pi, i, q, qIdx);
                desc = String.format("[Batas = $x_{%d}$] Evaluasi $x_{%d}$: Di luar! Masuk ke pencarian 3 titik batas.", qIdx, i);
            } else {
                desc = String.format("[Batas = $x_{%d}$] Evaluasi $x_{%d}$: Di dalam lingkaran sementara.", qIdx, i);
            }
            data.append(appender.recordStep(C, "step_" + step++, "blue", desc));
        }
        return C;
    }

    public Circle miniDiscWith2Points(PointSet P, int n, Point q1, int q1Idx, Point q2, int q2Idx){
        Point center = circleCenter2Points(q1, q2);
        Circle C = new Circle(center, q1);

        data.append(appender.recordStep(C, "step_" + step++, "blue", 
            String.format("Mulai Rekursi (2 Batas): Lingkaran awal dari batas pasti $x_{%d}$ dan $x_{%d}$.", q1Idx, q2Idx)
        ));

        int i;
        Point pi;
        for(i = 0; i < n; i++){
            pi = P.getPoint(i);
            String desc;
            if(!C.contains(pi)){
                Point center3 = circleCenter3Points(q1, q2, pi);
                C = new Circle(center3, q1);
                desc = String.format("[Batas = $x_{%d}, x_{%d}$] $x_{%d}$ di luar! Lingkaran dibentuk dari 3 titik batas.", q1Idx, q2Idx, i);
            } else {
                desc = String.format("[Batas = $x_{%d}, x_{%d}$] $x_{%d}$ aman di dalam.", q1Idx, q2Idx, i);
            }
            data.append(appender.recordStep(C, "step_" + step++, "blue", desc));
        }
        return C;
    }

    // 2 titik -> langsung aja titik tengah
    public Point circleCenter2Points(Point p, Point q){
        double Ux = (p.x + q.x) / 2.0;
        double Uy = (p.y + q.y) / 2.0;
        return new Point(Ux, Uy);
    }

    // 3 titik -> https://en.wikipedia.org/wiki/Circumcircle#Cartesian_coordinates_2
    public Point circleCenter3Points(Point p, Point q, Point r){
        double D = 2 * (p.x * (q.y - r.y) + q.x * (r.y - p.y) + r.x * (p.y - q.y));
        // atau D = 2 * CG.cross(p, q, r)
        if(D == 0){ // kolinear
            Point o1, o2;
            if (q.isBetween(p, r)) { // (p, q, r) ?
                o1 = p;
                o2 = r;
            } else if (p.isBetween(q, r)) { // (q, p, r) ?
                o1 = q;
                o2 = r;
            } else {
                // Jika bukan q dan bukan p yang di tengah, pasti r yang di tengah
                o1 = p;
                o2 = q;
            }
            return circleCenter2Points(o1, o2);
        }

        // TODO : sekarang ini pake math pow aja gpp, tapi ini bisa di perbaiki biar ga pake mathpow
        double Ux = ((Math.pow(p.x, 2) + Math.pow(p.y, 2)) * (q.y - r.y) + 
                     (Math.pow(q.x, 2) + Math.pow(q.y, 2)) * (r.y - p.y) + 
                     (Math.pow(r.x, 2) + Math.pow(r.y, 2)) * (p.y - q.y)) / D;
        double Uy = ((Math.pow(p.x, 2) + Math.pow(p.y, 2)) * (r.x - q.x) + 
                     (Math.pow(q.x, 2) + Math.pow(q.y, 2)) * (p.x - r.x) + 
                     (Math.pow(r.x, 2) + Math.pow(r.y, 2)) * (q.x - p.x)) / D;

        return new Point(Ux, Uy);
    }

    public String getNewLayer() {
        newLayer.setLength(0); // kosongin string sebelum diisi ulang
        
        newLayer.append("<layer name=\"titik_dasar\"/>\n");
        for (int i = 1; i <= step; i++) {
            newLayer.append(String.format("<layer name=\"step_%d\"/>\n", i));
        }
        newLayer.append("\n");
        
        // slide 1 : himpunan titik
        newLayer.append("<view layers=\"titik_dasar\" active=\"titik_dasar\"/>\n");
        
        // Slide > 2 : algoritma
        for (int i = 1; i <= step; i++) {
            newLayer.append(String.format("<view layers=\"titik_dasar step_%d\" active=\"step_%d\"/>\n", i, i));
        }
        
        return newLayer.toString();
    }

    public String getData() {
        return data.toString();
    }
}
