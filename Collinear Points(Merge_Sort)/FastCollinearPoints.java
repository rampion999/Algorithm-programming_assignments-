
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

    private int segCnt;
    private LineSegment[] segmentsArr;
    

    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 points
        
        Point[] dut = checkIntegrity(points);

        LinkedList<Point[]> segLL = new LinkedList<Point[]>();

        

        if (dut.length > 3) {
            boolean done;
            for (int pIdx = 0; pIdx < dut.length; pIdx++) {
                
                Comparator<Point> cmprP = dut[pIdx].slopeOrder();


                Point[] qPoints = new Point[dut.length - 1];
                int cnt = 0;
                for (int i = 0; i < dut.length; i++) {
                    if (i != pIdx) {
                        qPoints[cnt] = dut[i];
                        cnt++;
                    }
                }
                

                // LinkedList<Point[]> tbdLine = new LinkedList<Point[]>();
                // for (Point[] pair : segLL) {
                //     if (pointInLine(pair, dut[pIdx])) {
                //         tbdLine.add(pair);
                //     }
                // }

                // LinkedList<Point> tbdPoint = new LinkedList<Point>();
                // boolean inLine = false;
                // for (int i = 0; i < dut.length; i++) {
                //     if (i != pIdx) {
                //         for (Point[] pair : tbdLine) {
                //             if (pointInLine(pair, dut[i])) {
                //                 inLine = true;
                //                 break;
                //             }
                //         }
                //         if (!inLine) {
                //             tbdPoint.add(dut[i]);
                //         }
                //     }
                // }

                // if (tbdPoint.size() >= 3) {
                //     Point[] qPoints = new Point[tbdPoint.size()];
                //     int c = 0;
                //     for (Point point : tbdPoint) {
                //         qPoints[c++] = point;
                //     }
                    
                    
                // }

                done = mergeSortBySlope(qPoints, cmprP, segLL, dut[pIdx], dut.length);
                if (done) {
                    break;
                }
            }
        }
        
        segmentsArr = new LineSegment[segLL.size()];
        segCnt = 0;
        for (Point[] pair : segLL) {
            segmentsArr[segCnt] = new LineSegment(pair[0], pair[1]);
            segCnt++;
        }
    }


    private Point[] checkIntegrity(Point[] points) {
        if (points == null || points.length == 0 || points[0] == null) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i] == null || points[j] == null || points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }   
        }

        return points.clone();
    }




    public int numberOfSegments() {        // the number of line segments
        return segCnt;
    }
    public LineSegment[] segments() {               // the line segments
        LineSegment[] tmp = segmentsArr.clone();
        return tmp;
    }

    private static boolean mergeSortBySlope(Point[] points, Comparator<Point> cmprP, LinkedList<Point[]> segLL, Point p, int max) {
        Point[] aux = new Point[points.length];
        int lo = 0;
        int hi = points.length - 1;
        int mid = lo + (hi - lo) / 2;

        for (int k = lo; k <= hi; k++) {
            aux[k] = points[k];
        }

        sort(points, aux, lo, mid, cmprP);
        sort(points, aux, mid + 1, hi, cmprP);
        return findSeg(points, cmprP, segLL, p, max);
    }

    private static boolean findSeg(Point[] points, Comparator<Point> cmprP, LinkedList<Point[]> segLL, Point p, int max) {
        Point start;
        int hi = points.length - 1;
        int i = 0;
        int mid = hi / 2;
        int j = mid + 1;

        LinkedList<Point> tbd = new LinkedList<Point>();

        while (i <= mid || j <= hi) {

            tbd.clear();

            if (i > mid) {
                start = points[j++];
            } else if (j > hi) {
                start = points[i++];
            } else if (cmprP.compare(points[i], points[j]) <= 0) {
                start = points[i++];
            } else {
                start = points[j++];
            }

            tbd.add(start);


            while (i <= mid && cmprP.compare(start, points[i]) == 0) {         
                tbd.add(points[i]);
                i++;
            }

            
            while (j <= hi && cmprP.compare(start, points[j]) == 0) {
                tbd.add(points[j]);
                j++;
            }

            if (tbd.size() >= 3) {
                Point[] segPoints = new Point[tbd.size() + 1];
                segPoints[0] = p;
                int tbdCnt = 1;
                for (Point point : tbd) {
                    segPoints[tbdCnt] = point;
                    tbdCnt++;
                }
                

                Arrays.sort(segPoints);

                if (segPoints[0].compareTo(p) == 0) {
                    Point[] pair = {p, segPoints[segPoints.length - 1]};
                    segLL.add(pair);
                    if (segPoints.length == max) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private static void sort(Point[] a, Point[] aux, int lo, int hi, Comparator<Point> cmprP) {
        if (hi <= lo) {
            return;
        }
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid, cmprP);
        sort(a, aux, mid + 1, hi, cmprP);
        merge(a, aux, lo, mid, hi, cmprP);
    }

    private static void merge(Point[] a, Point[] aux, int lo, int mid, int hi, Comparator<Point> cmprP) {
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }
        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            }
            else if (j > hi) {
                a[k] = aux[i++];
            }
            else if (cmprP.compare(aux[i], aux[j]) <= 0) {
                a[k] = aux[i++];
            }
            else {
                a[k] = aux[j++];
            }
        }
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
