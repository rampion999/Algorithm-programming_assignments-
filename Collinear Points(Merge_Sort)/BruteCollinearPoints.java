
import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

    private int segCnt = 0;
    private LineSegment[] segmentsArr;


    public BruteCollinearPoints(Point[] points) {    // finds all line segments containing 4 points
        
        if (points == null || points.length == 0) {
            throw new IllegalArgumentException();
        }

        Point[] dut = points.clone();
        checkIntegrity(dut);

        Point[][] pairs = new Point[dut.length][2];
        for (int p = 0; p < dut.length - 3; p++) {
            Comparator<Point> cmprP = dut[p].slopeOrder();
            for (int q = p + 1; q < dut.length - 2; q++) {
                for (int r = q + 1; r < dut.length - 1; r++) {             
                    if (cmprP.compare(dut[q], dut[r]) == 0) {
                        for (int s = r + 1; s < dut.length; s++) {
                            if (cmprP.compare(dut[q], dut[s]) == 0) {
                                pairs[segCnt][0] = dut[p];
                                pairs[segCnt][1] = dut[s];
                                segCnt += 1;
                            }
                        }
                    }
                }
            }
        }
        
        segmentsArr = new LineSegment[segCnt];
        for (int i = 0; i < segCnt; i++) {
            segmentsArr[i] = new LineSegment(pairs[i][0], pairs[i][1]);
        }
    }

    private static void checkIntegrity(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }

        Arrays.sort(points);
        Point curr = points[0];
        for (int i = 1; i < points.length; i++) {
            if (curr.compareTo(points[i]) == 0) {
                throw new IllegalArgumentException();
            }
            curr = points[i];
        }
    }

    public int numberOfSegments() {        // the number of line segments
        return segCnt;
    }
    public LineSegment[] segments() {               // the line segments
        LineSegment[] tmp = segmentsArr.clone();
        return tmp;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
