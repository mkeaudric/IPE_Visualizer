package XMLUtil;

import GeoUtil.Circle;
import GeoUtil.Point;
import GeoUtil.PointSet;

public class AppendIpeData {
    // TODO : ganti parameter jadi objek, lalu buat deteksi objek untuk menyesuaikan format yang dipakai, jadi tidak hanya bisa untuk Circle saja
    public String recordStep(Circle c, String layerName, String color) {
        if (c == null) return ""; 
        
        double r = Math.sqrt(c.getRadiusSquared()); 
        
        return String.format(
            "<path layer=\"%s\" stroke=\"%s\">\n%.4f 0 0 %.4f %.4f %.4f e\n</path>\n",
            layerName, color, r, r, c.getCenter().x, c.getCenter().y
        );
    }

    public String circleToIpe(Circle c, String color, String layerName){
        return recordStep(c, layerName, color);
    }

    public String pointToIpe(Point p, String color, String layerName) {
        return String.format(
            "<use layer=\"%s\" name=\"mark/disk(sx)\" pos=\"%.2f %.2f\" size=\"normal\" stroke=\"%s\"/>\n",
            layerName, p.x, p.y, color
        );
    }

    public String pointSetToIpe(PointSet P, String color, String layerName) {
        StringBuilder res = new StringBuilder();
        int len = P.getSize();
        
        for (int i = 0; i < len; i++) {
            // Ambil koordinat aslinya
            double px = P.getPoint(i).x;
            double py = P.getPoint(i).y;
            
            // 1. Gambar titik (disk) di posisi aslinya
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
