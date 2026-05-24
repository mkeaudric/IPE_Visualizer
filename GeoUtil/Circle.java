public class Circle {
    // simpen titik tengah & radius
    private Point center;
    private double radiusSquared;

    public Circle(Point center, Point o){
        this.center = center;
        this.radiusSquared = center.distanceSquared(o);
    }

    public boolean contains(Point p){
        return p.distanceSquared(this.center) <= this.radiusSquared + CG.EPS;
    }

    public double getRadiusSquared(){
        return this.radiusSquared;
    }

    public double getRadius(){
        return Math.sqrt(this.radiusSquared);
    }

    public Point getCenter(){
        return this.center;
    }
}
