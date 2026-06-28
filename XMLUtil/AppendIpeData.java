package XMLUtil;

import GeoUtil.Circle;
import GeoUtil.Point;
import GeoUtil.PointSet;

public class AppendIpeData {

    public String recordStep(Object obj, String layerName, String color, String desc) {
        if (obj == null) return ""; 
        
        StringBuilder res = new StringBuilder();
        
        if (obj instanceof Circle) {
            Circle c = (Circle) obj;
            double r = Math.sqrt(c.getRadiusSquared()); 
            res.append(String.format(
                "<path layer=\"%s\" stroke=\"%s\">\n%.4f 0 0 %.4f %.4f %.4f e\n</path>\n",
                layerName, color, r, r, c.getCenter().x, c.getCenter().y
            ));
        } 
        else if (obj instanceof Point) {
            Point p = (Point) obj;
            res.append(String.format(
                "<use layer=\"%s\" name=\"mark/disk(sx)\" pos=\"%.2f %.2f\" size=\"normal\" stroke=\"%s\"/>\n",
                layerName, p.x, p.y, color
            ));
        }
        else if (obj instanceof PointSet) {
            PointSet ps = (PointSet) obj;
            // Panggil fungsi pointSetToIpe yang sudah kamu buat di bawah
            res.append(pointSetToIpe(ps, color, layerName));
        }
        // TODO : else if (obj instanceof Line / Polygon) { ... logic buat Convex Hull ... }

        // teks Penjelasan (MiniPage LaTeX) di kanan bawah
        // cek apakah desc ada isinya agar tidak muncul kotak kosong jika dipanggil tanpa deskripsi
        if (desc != null && !desc.isEmpty()) {
            res.append(String.format(
                "<text layer=\"%s\" transformations=\"translations\" pos=\"980 50\" stroke=\"black\" type=\"minipage\" width=\"350\" halign=\"right\" valign=\"bottom\">\n" +
                "\\begin{flushright}\n" +
                "{\\bf \\color{blue} Status Algoritma}\\\\\n" + // tulisan umum agar bisa dipakai algoritma lain
                "\\medskip\n" +
                "%s\n" +
                "\\end{flushright}\n" +
                "</text>\n",
                layerName, desc
            ));
        }

        return res.toString();
    }

    // helper cetak tanpa deskripsi (string kosong "")
    public String circleToIpe(Circle c, String color, String layerName){
        return recordStep(c, layerName, color, "");
    }

    public String pointToIpe(Point p, String color, String layerName) {
        return recordStep(p, layerName, color, "");
    }

    public String pointSetToIpe(PointSet P, String color, String layerName) {
        StringBuilder res = new StringBuilder();
        int len = P.getSize();
        
        for (int i = 0; i < len; i++) {
            double px = P.getPoint(i).x;
            double py = P.getPoint(i).y;
            
            res.append(String.format(
                "<use layer=\"%s\" name=\"mark/disk(sx)\" pos=\"%.2f %.2f\" size=\"normal\" stroke=\"%s\"/>\n",
                layerName, px, py, color
            ));

            res.append(String.format(
                "<text layer=\"%s\" transformations=\"translations\" pos=\"%.2f %.2f\" stroke=\"%s\" type=\"label\" valign=\"baseline\" style=\"math\">x_{%d}</text>\n",
                layerName, px + 5.0, py + 5.0, color, i
            ));
        }
        return res.toString();
    }
}