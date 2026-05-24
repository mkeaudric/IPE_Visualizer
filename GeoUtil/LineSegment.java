public class LineSegment {
    Point start;
    Point end;

    LineSegment(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    double distanceToPoint(Point p) {
        Point a = this.start;
        Point b = this.end;
        double dist;
        if(CG.dot(a, p, b) < 0){
            dist = p.distanceToOtherPoints(a);
        } else if(CG.dot(b, p, a) < 0){
            dist = p.distanceToOtherPoints(b);
        } else{
            dist = Math.abs(CG.cross(a, p, b)) / a.distanceToOtherPoints(b);
        }

        return dist;
    }

    double leftTurnToPoint(Point target) {
		double res = 0.0;
		res = CG.cross(this.start, this.end, target);
        return res; //return 0 jika lurus, plus/minus jika belok kanan/kiri,  
    }
    
    boolean isIntersect(LineSegment other) {
        // pake 2 cross di tiap segmen
        // segmen this pake a, b
        // segmen other pake p, q
		
        Point a = this.start;
        Point b = this.end;
        Point p = other.start;
        Point q = other.end;

        // degenerate case : 2 segmen di satu garis lurus yang sama (cross nya 0 tp belum tentu motong)
        if (CG.cross(a, p, b) == 0 && CG.cross(a, q, b) == 0 && 
            CG.cross(p, a, q) == 0 && CG.cross(p, b, q) == 0) {
            
            // cek rentang x dan rentang y overlap ga
            // sumber : https://stackoverflow.com/questions/3269434/whats-the-most-efficient-way-to-test-if-two-ranges-overlap
            // cari maks titik terkiri ab dan titik terkiri pq
            // cari min titik terkanan ab dan titik terkanan pq
            // kalo maks <= min motong
            // juga buat yg y
            boolean overlapX = Math.max(Math.min(a.x, b.x), Math.min(p.x, q.x)) <= Math.min(Math.max(a.x, b.x), Math.max(p.x, q.x));
            boolean overlapY = Math.max(Math.min(a.y, b.y), Math.min(p.y, q.y)) <= Math.min(Math.max(a.y, b.y), Math.max(p.y, q.y));
            
            // kalo overlap berarti motong (true), kalo ga false
            return overlapX && overlapY;
        }

        // pertama cek apakah cross apb dan aqb berbeda (bisa cross apb <= 0 dan cross aqb >= 0, atau sebaliknya)
        // disebut di soal ujung di garis dianggap berpotongan, jadi pake <= 0 dan >= 0
        if((CG.cross(a, p, b) <= 0 && CG.cross(a, q, b) >= 0) || (CG.cross(a, p, b) >= 0 && CG.cross(a, q, b) <= 0))
            // kalau masuk kondisi benar, cek lagi dari segmen paq dan pbq dengan cara yg sama
            if((CG.cross(p, a, q) <= 0 && CG.cross(p, b, q) >= 0) || (CG.cross(p, a, q) >= 0 && CG.cross(p, b, q) <= 0))
                // kalau udah cek kedua segmen dan kondisi terpenuhi, berarti berpotongan
                return true;

        return false; // ga berpotongan
    }

}