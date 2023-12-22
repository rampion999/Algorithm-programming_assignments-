
import java.util.LinkedList;
import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {

    private TreeSet<Point2D> pointTreeSet;
    private RectHV unitRec;

    // construct an empty set of points 
    public PointSET() {
        pointTreeSet = new TreeSet<Point2D>();
        unitRec = new RectHV(0, 0.0, 1, 1);
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointTreeSet.isEmpty();
    }

    // number of points in the set 
    public int size() {
        return pointTreeSet.size();
    }
    
    // add the point to the set (if it is not already in the set)
    // Performance: worst case better than "O(log N)"
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (unitRec.contains(p) && !pointTreeSet.contains(p)) {
            pointTreeSet.add(p);
        }
    }               
    
    // does the set contain point p? 
    // Performance: worst case better than "O(log N)"
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (!unitRec.contains(p)) {
            return false;
        }
        return pointTreeSet.contains(p);
    }

    // draw all points to standard draw 
    public void draw() {
        for (Point2D p : pointTreeSet) {
            p.draw();
        }
    }
    
    // all points that are inside the rectangle (or on the boundary) 
    // Performance: worst case better than "O(N)"
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        LinkedList<Point2D> results = new LinkedList<Point2D>();
        for (Point2D p : this.pointTreeSet) {
            if (rect.contains(p)) {
                results.add(p);
            }
        }
        
        return results;
    }

    // a nearest neighbor in the set to point p; null if the set is empty 
    // Performance: worst case better than "O(N)"
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        double tDistance = 0;
        Point2D target = null;
        for (Point2D dut : pointTreeSet) {
            double dutDis = p.distanceSquaredTo(dut);
            if (target == null || dutDis < tDistance) {
                target = dut;
                tDistance = dutDis;
            }
        }
        return target;
    }

    public static void main(String[] args) {
        PointSET t = new PointSET();
        t.insert(new Point2D(0.15, 0.15));
        t.insert(new Point2D(0.8, 0.2));
        t.insert(new Point2D(0.05, 0.2));
        t.insert(new Point2D(0.6, 0.6));
        t.insert(new Point2D(0.999, 0.58));
        t.insert(new Point2D(0.999, 0.55));
        t.insert(new Point2D(0.3, 0.3));
        t.insert(new Point2D(0.6, 0.6));
        t.insert(new Point2D(0.05, 0.05));
        t.draw();
        RectHV rec = new RectHV(0.1, 0.1, 0.5, 0.5);
        rec.draw();
        for (Point2D p : t.range(rec)) {
            StdOut.println(p.toString());
        }
        
        StdOut.println("========================");
        StdOut.println("t.nearest(new Point2D(0.6, 0.59))");
        StdOut.println(t.nearest(new Point2D(0.6, 0.59)));

        StdOut.println("========================");
        StdOut.println("t.nearest(new Point2D(0.31, 0.3))");
        StdOut.println(t.nearest(new Point2D(0.31, 0.3)));

        StdOut.println("========================");
        StdOut.println("t.nearest(new Point2D(0.05, 0.2))");
        StdOut.println(t.nearest(new Point2D(0.05, 0.2)));

        StdOut.println("========================");
        StdOut.println("t.nearest(new Point2D(0.57, 0.57))");
        StdOut.println(t.nearest(new Point2D(0.57, 0.57)));

    }
}