

import java.util.LinkedList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {

    private static final boolean X = true;
    private static final boolean Y = false;

    private int size;
    private Node root;
    private RectHV unitRec;

    private class Node {
        private boolean coordinate;
        private Node left;
        private Node right;
        private Point2D point;
        
        private Node(boolean c, Point2D p) {
            this.coordinate = c;
            this.point = p;
            this.left = null;
            this.right = null;
        }

        private Point2D getPoint() {
            return this.point;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
        unitRec = new RectHV(0, 0.0, 1, 1);
        size = 0;
    }     
    
    // is the set empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // number of points in the set
    public int size() {
        return this.size;
    }

    // add the point to the set (if it is not already in the set)
    // Performance: worst case better than "O(log N)"
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (unitRec.contains(p) && !this.contains(p)) {
            root = insert(root, X, p);
        }
    }
    
    private Node insert(Node node, boolean c, Point2D p) {
        if (node == null) {
            this.size += 1;
            return new Node(c, p);
        }

        if (ifToLeft(node, p)) {
            node.left = insert(node.left, !c, p);
        } else {
            node.right = insert(node.right, !c, p);
        }
        return node;
    }

    private static boolean ifToLeft(Node node, Point2D p) {
        double nodeCmpVal;
        double pointCmpVal;
        if (node.coordinate == X) {
            nodeCmpVal = node.point.x();
            pointCmpVal = p.x();
        } else {
            nodeCmpVal = node.point.y();
            pointCmpVal = p.y();
        }
        return pointCmpVal <= nodeCmpVal;
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
        return search(this.root, p);
    }
    
    private static boolean search(Node node, Point2D p) {
        if (node == null) {
            return false;
        }
        if (node.point.equals(p)) {
            return true;
        } else if (ifToLeft(node, p)) {
            return search(node.left, p);
        } else {
            return search(node.right, p);
        }
    }
    
    // draw all points to standard draw 
    public void draw() {
        drawPic(root, 0, 0, 1, 1);
    }
    
    private static void drawPic(Node node, double minX, double minY, double maxX, double maxY) {
        if (node != null) {
            double leftMaxX;
            double leftMaxY;
            double rightMinX;
            double rightMinY;
            StdDraw.setPenRadius(0.01);
            if (node.coordinate == X) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.point.x(), minY, node.point.x(), maxY);
                leftMaxX  = node.point.x();
                leftMaxY  = maxY;
                rightMinX = node.point.x();
                rightMinY = minY;
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(minX, node.point.y(), maxX, node.point.y());
                leftMaxX  = maxX;
                leftMaxY  = node.point.y();
                rightMinX = minX;
                rightMinY = node.point.y();
            }
            StdDraw.setPenColor(StdDraw.BLACK);
            node.point.draw();
            drawPic(node.left, minX, minY, leftMaxX, leftMaxY);
            drawPic(node.right, rightMinX, rightMinY, maxX, maxY);
        }
    }
    
    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        LinkedList<Point2D> col = new LinkedList<Point2D>();
        pruningRange(rect, col, root, unitRec);
        return col;
    }

    private static void pruningRange(RectHV tarRect, LinkedList<Point2D> col, Node node, RectHV dutRect) {
        if (node != null) {
            if (tarRect.contains(node.point)) {
                col.add(node.point);
            }

            // Check left
            RectHV rectLeft = findRect(node, dutRect, "left");
            if (tarRect.intersects(rectLeft)) {
                pruningRange(tarRect, col, node.left, rectLeft);
            }

            // Check right
            RectHV rectRight = findRect(node, dutRect, "right");
            if (tarRect.intersects(rectRight)) {
                pruningRange(tarRect, col, node.right, rectRight);
            }
        } 
    }

    private static RectHV findRect(Node node, RectHV dutRec, String dir) {
        if (dir.equals("left")) {
            if (node.coordinate == X) {
                return new RectHV(dutRec.xmin(), dutRec.ymin(), node.point.x(), dutRec.ymax());
            } else {
                return new RectHV(dutRec.xmin(), dutRec.ymin(), dutRec.xmax(), node.point.y());
            }
        } else {
            if (node.coordinate == X) {
                return new RectHV(node.point.x(), dutRec.ymin(), dutRec.xmax(), dutRec.ymax());
            } else {
                return new RectHV(dutRec.xmin(), node.point.y(), dutRec.xmax(), dutRec.ymax());
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (this.size == 0) {
            return null;
        }
        Node result = pruningNearest(root, unitRec, root, root.point.distanceSquaredTo(p), p);
        if (result == null) {
            return null;
        } else {
            return result.getPoint();
        }
    }

    private static boolean queryCheck(Node dutNode, RectHV dutRec, double lastWinDis, Point2D p) {
        return !(dutNode == null || dutRec.distanceSquaredTo(p) > lastWinDis);
    }

    private static Node pruningNearest(Node dutNode, RectHV dutRec, Node lastWinNode, double lastWinDis, Point2D p) {
        if (dutNode == null) {
            return lastWinNode;
        }

        // Check query point
        Node winNode  = lastWinNode;
        double winDis = lastWinDis;
        double dutDis = dutNode.point.distanceSquaredTo(p);
        if (dutDis < winDis) {
            winNode = dutNode;
            winDis  = dutDis;
        }

        // Search near side first. The other side will only be query if pass the query check.
        RectHV leftSide = findRect(dutNode, dutRec, "left");
        RectHV rightSide = findRect(dutNode, dutRec, "right");
        if (leftSide.distanceSquaredTo(p) < rightSide.distanceSquaredTo(p)) {
            winNode = pruningNearest(dutNode.left, leftSide, winNode, winDis, p);
            winDis = winNode.point.distanceSquaredTo(p);
            if (queryCheck(dutNode, rightSide, winDis, p)) {
                winNode = pruningNearest(dutNode.right, rightSide, winNode, winDis, p);
            }
        } else {
            winNode = pruningNearest(dutNode.right, rightSide, winNode, winDis, p);
            winDis = winNode.point.distanceSquaredTo(p);
            if (queryCheck(dutNode, leftSide, winDis, p)) {
                winNode = pruningNearest(dutNode.left, leftSide, winNode, winDis, p);
            }
        }
        return winNode;
    }

    public static void main(String[] args) {
        // KdTree t = new KdTree();


        // t.insert(new Point2D(0.7, 0.2));
        // t.insert(new Point2D(0.5, 0.4));
        // t.insert(new Point2D(0.2, 0.3));
        // t.insert(new Point2D(0.4, 0.7));
        // t.insert(new Point2D(0.9, 0.6));
        // t.insert(new Point2D(0.6, 0.8));
        // t.insert(new Point2D(0.55, 0.75));
        // StdDraw.setPenRadius(0.01);
        // t.draw();

        // StdOut.println("========================");
        // StdOut.println("t.contains(new Point2D(0.9, 0.6))");
        // StdOut.println(t.contains(new Point2D(0.9, 0.6)));

        // StdOut.println("========================");
        // StdOut.println("t.contains(new Point2D(0.9, 0.777))");
        // StdOut.println(t.contains(new Point2D(0.9, 0.777)));



        // t.insert(new Point2D(0.15, 0.15));
        // t.insert(new Point2D(0.8, 0.2));
        // t.insert(new Point2D(0.05, 0.2));
        // t.insert(new Point2D(0.6, 0.6));
        // t.insert(new Point2D(0.999, 0.58));
        // t.insert(new Point2D(0.999, 0.55));
        // t.insert(new Point2D(0.3, 0.3));
        // t.insert(new Point2D(0.6, 0.6));
        // t.insert(new Point2D(0.05, 0.05));

        // StdOut.println("========================");
        // StdOut.println("t.nearest(new Point2D(0.6, 0.59))");
        // StdOut.println(t.nearest(new Point2D(0.6, 0.59)));

        // StdOut.println("========================");
        // StdOut.println("t.nearest(new Point2D(0.31, 0.3))");
        // StdOut.println(t.nearest(new Point2D(0.31, 0.3)));

        // StdOut.println("========================");
        // StdOut.println("t.nearest(new Point2D(0.05, 0.2))");
        // StdOut.println(t.nearest(new Point2D(0.05, 0.2)));

        // StdOut.println("========================");
        // StdOut.println("t.nearest(new Point2D(0.57, 0.57))");
        // StdOut.println(t.nearest(new Point2D(0.57, 0.57)));
        // t.draw();

        // StdOut.println("========================");
        // StdOut.println("t.range(0.1, 0.1, 0.85, 0.85)");
        // RectHV rec = new RectHV(0.1, 0.1, 0.85, 0.85);
        // for (Point2D p : t.range(rec)) {
        //     StdOut.println(p.toString());
        // }

        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }
        // StdOut.println("nearest(new Point2D(0.34, 0.94))");
        // kdtree.draw();
        
        // StdOut.println(kdtree.nearest(new Point2D(0.66, 0.34)));

        // KdTree kdtree = new KdTree();
        // kdtree.insert(new Point2D(0.372, 0.497));
        // kdtree.insert(new Point2D(0.564, 0.413));
        // kdtree.insert(new Point2D(0.226, 0.577));
        // kdtree.insert(new Point2D(0.144, 0.179));
        // kdtree.insert(new Point2D(0.083, 0.51));
        // kdtree.insert(new Point2D(0.32, 0.708));
        // kdtree.insert(new Point2D(0.417, 0.362));
        // kdtree.insert(new Point2D(0.862, 0.825));
        // kdtree.insert(new Point2D(0.785, 0.725));
        // kdtree.insert(new Point2D(0.499, 0.208));

        // kdtree.nearest(new Point2D(0.21, 0.35));
        // StdOut.println(kdtree.nearest(new Point2D(0.52, 0.75)));


        // KdTree kdtree = new KdTree();
        // kdtree.insert(new Point2D(0.7, 0.2));
        // kdtree.insert(new Point2D(0.5, 0.4));
        // kdtree.insert(new Point2D(0.2, 0.3));
        // kdtree.insert(new Point2D(0.4, 0.7));
        // kdtree.insert(new Point2D(0.9, 0.6));
        // kdtree.nearest(new Point2D(0.705, 0.965));
    }
}