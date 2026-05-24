package GeoUtil;
public class Point {
    public double x;
    public double y;

    public Point (double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceSquared(Point o) { // lebih murah, gapake Math.sqrt()
        double dx = this.x - o.x;
        double dy = this.y - o.y;
        return (dx * dx) + (dy * dy); 
    }

    double distanceToOtherPoints(Point o) {
        // kartesian
        double dx = this.x - o.x;
        double dy = this.y - o.y;
        return Math.sqrt(dx*dx + dy*dy);
    }

    double distanceToLineSegment(LineSegment l) {
        //panggil aja dari l, l.distanceToPoint(this);
        return l.distanceToPoint(this);
    }

    public boolean isBetween(Point p1, Point p2){
        boolean xBetween = this.x >= Math.min(p1.x, p2.x) && this.x <= Math.max(p1.x, p2.x); // secara vertikal, apakah q ada di tengah p dan r
        boolean yBetween = this.y >= Math.min(p1.y, p2.y) && this.y <= Math.max(p1.y, p2.y); // secara horizontal, apakah q ada di tengah p dan r
        return xBetween && yBetween;
    }
}
