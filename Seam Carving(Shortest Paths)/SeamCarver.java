
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.LinkedList;

public class SeamCarver {

    private Picture currPic;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {        
        if (picture == null) {
            throw new IllegalArgumentException();
        }

        currPic = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(currPic);
    }

    // width of current picture
    public int width() {
        return currPic.width();
    }

    // height of current picture
    public int height() {
        return currPic.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > currPic.width() - 1 || y < 0 || y > currPic.height() - 1) {
            throw new IllegalArgumentException();
        }

        if (x == 0 || x == currPic.width() - 1 || y == 0 || y == currPic.height() - 1) {
            return (double) 1000;
        }

        int dgSquareX = duleGradient(currPic.get(x - 1, y), currPic.get(x + 1, y));
        int dgSquareY = duleGradient(currPic.get(x, y - 1), currPic.get(x, y + 1));
        return Math.sqrt(dgSquareX + dgSquareY);
    }

    private static int duleGradient(Color front, Color back) {
        int r = back.getRed() - front.getRed();
        int g = back.getGreen() - front.getGreen();
        int b = back.getBlue() - front.getBlue();
        return r * r + g * g + b * b;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // convert 2d to 1d, 
        // use index 0 to be the start point which is point to all points in row 0.
        // use index "currPic.width() * currPic.height() + 1" to be the end point 
        // which is pointed from all points in last row.
        int endIdx = currPic.width() * currPic.height() + 1;
        int[] pointTo = new int[endIdx + 1];
        int[] distTo = new int[endIdx + 1];
        double[] energyTo = new double[endIdx + 1];
        
        // Initial value
        for (int i = 1; i <= endIdx; i++) {
            energyTo[i] = Double.POSITIVE_INFINITY;
        }

        // deal with start point
        for (int x = 0; x < currPic.width(); x++) {
            relax(pointTo, energyTo, distTo, 0, axisToIndex(x, 0), energy(x, 0));
        }

        // Deal with rest point. From upper left to bottom right. So it is a topological order.
        // It doesn't need another queue to determine next point to explore and relax.
        double dutEng;
        for (int y = 0; y < currPic.height(); y++) {

            // if it is not last row. Create a energy array to store the energy of next row.
            double[] dutEnergy = new double[currPic.width()];
            if (y != currPic.height() - 1) {   
                for (int x = 0; x < currPic.width(); x++) {
                    dutEnergy[x] = energy(x, y + 1);
                }
            }

            // relax all adjacent point in the row.
            for (int x = 0; x < currPic.width(); x++) {
                for (int adjIdx : adjIdx(x, y, true)) {
                    
                    int[] axis = indexToAxis(adjIdx);
                    if (adjIdx == endIdx) {
                        // the end point use arbitrary number.
                        dutEng =  1;
                    } else {
                        dutEng = dutEnergy[axis[0]];
                    }
                    relax(pointTo, energyTo, distTo, axisToIndex(x, y), adjIdx, dutEng);
                }
            }
        }

        // Collct shortest path itoratly using pointTo array.
        int[] ans = new int[distTo[endIdx] - 1];
        int idx = pointTo[endIdx];
        for (int i = ans.length - 1; i >= 0; i--) {
            ans[i] = indexToAxis(idx)[0];
            idx = pointTo[idx];
        }
        return ans;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int endIdx = currPic.width() * currPic.height() + 1;
        int[] pointTo = new int[endIdx + 1];
        int[] distTo = new int[endIdx + 1];
        double[] energyTo = new double[endIdx + 1];
        
        // Initial value
        for (int i = 1; i <= endIdx; i++) {
            energyTo[i] = Double.POSITIVE_INFINITY;
        }
        

        // deal with start point
        for (int y = 0; y < currPic.height(); y++) {
            relax(pointTo, energyTo, distTo, 0, axisToIndex(0, y), energy(0, y));
        }

        // rest
        double dutEng;
        for (int x = 0; x < currPic.width(); x++) {
            double[] dutEnergy = new double[currPic.height()];
            if (x != currPic.width() - 1) {   
                for (int y = 0; y < currPic.height(); y++) {
                    dutEnergy[y] = energy(x + 1, y);
                }
            }
            
            for (int y = 0; y < currPic.height(); y++) {
                for (int adjIdx : adjIdx(x, y, false)) {
                    int[] axis = indexToAxis(adjIdx);
                    if (adjIdx == endIdx) {
                        dutEng =  1;
                    } else {
                        // dutEng =  energy(axis[0], axis[1]);
                        dutEng = dutEnergy[axis[1]];
                    }
                    relax(pointTo, energyTo, distTo, axisToIndex(x, y), adjIdx, dutEng);
                }
            }
        }

        int[] ans = new int[distTo[endIdx] - 1];
        int idx = pointTo[endIdx];
        for (int i = ans.length - 1; i >= 0; i--) {
            ans[i] = indexToAxis(idx)[1];
            idx = pointTo[idx];
        }
        return ans;
    }

    private void relax(int[] pointTo, double[] energyTo, int[] distTo, int vIdx, int wIdx, double energy) {
        if (energyTo[wIdx] > energyTo[vIdx] + energy) {
            energyTo[wIdx] = energyTo[vIdx] + energy;
            distTo[wIdx] = distTo[vIdx] + 1;

            pointTo[wIdx] = vIdx;
        }
    }

    private int axisToIndex(int x, int y) {
        return y * currPic.width() + x + 1;
    }

    private int[] indexToAxis(int index) {
        if (index % currPic.width() == 0) {
            return new int[] {currPic.width() - 1, (index / currPic.width()) - 1};
        }
        return new int[] {(index - 1) % currPic.width(), index / currPic.width()};
    }

    private Iterable<Integer> adjIdx(int x, int y, boolean vertical) {
        LinkedList<Integer> adjs = new LinkedList<Integer>();
        if (vertical) {
            if (y == currPic.height() - 1) {
                adjs.add(currPic.width() * currPic.height() + 1);
                return adjs;
            }
            
            // down
            adjs.add(axisToIndex(x, y + 1));
            
            if (x != 0) {
                // lower left
                adjs.add(axisToIndex(x - 1, y + 1));
            }

            if (x != currPic.width() - 1) {
                // lower right
                adjs.add(axisToIndex(x + 1, y + 1));
            }
        } else {
            if (x == currPic.width() - 1) {
                adjs.add(currPic.width() * currPic.height() + 1);
                return adjs;
            }
            
            // right
            adjs.add(axisToIndex(x + 1, y));
            
            if (y != 0) {
                // upper right
                adjs.add(axisToIndex(x + 1, y - 1));
            }

            if (y != currPic.height() - 1) {
                // lower right
                adjs.add(axisToIndex(x + 1, y + 1));
            }
        }
        return adjs;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        // Check if height of the picture is less than or equal to 1.
        if (seam == null || currPic.height() <= 1 || currPic.width() != seam.length) {
            throw new IllegalArgumentException();
        }

        // Check either an entry is outside its prescribed range or two adjacent entries differ by more than 1
        int preIdx = seam[0];
        for (int i = 0; i < seam.length; i++) {
            if ((seam[i] < 0 || seam[i] > currPic.height() - 1) || (seam[i] > preIdx + 1 || seam[i] < preIdx - 1)) {
                throw new IllegalArgumentException();
            }
            preIdx = seam[i];
        }
        
        Picture newPic = new Picture(currPic.width(), currPic.height() - 1);
        for (int x = 0; x < currPic.width(); x++) {
            int newY = 0;
            for (int y = 0; y < currPic.height(); y++) {
                if (y == seam[x]) {
                    continue;
                }
                newPic.set(x, newY, currPic.get(x, y));
                newY++;       
            }
        }
        currPic = newPic;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        // Check if width of the picture is less than or equal to 1
        if (seam == null || currPic.width() <= 1 || currPic.height() != seam.length) {
            throw new IllegalArgumentException();
        }

        // Check if either an entry is outside its prescribed range or two adjacent entries differ by more than 1
        int preIdx = seam[0];
        for (int i = 0; i < seam.length; i++) {
            if ((seam[i] < 0 || seam[i] > currPic.width() - 1) || (seam[i] > preIdx + 1 || seam[i] < preIdx - 1)) {
                throw new IllegalArgumentException();
            }
            preIdx = seam[i];
        }

        Picture newPic = new Picture(currPic.width() - 1, currPic.height());
        for (int y = 0; y < currPic.height(); y++) {
            int newX = 0;
            for (int x = 0; x < currPic.width(); x++) {
                if (x == seam[y]) {
                    continue;
                }
                newPic.set(newX, y, currPic.get(x, y));
                newX++;     
            }
        }
        currPic = newPic;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        // Picture in = new Picture("6x5.png");

        Picture in = new Picture(args[0]);

        SeamCarver t = new SeamCarver(in);

        

        StdOut.println("findVerticalSeam: ");
        int[] arr = t.findVerticalSeam();
        for (int i = 0; i < arr.length; i++) {
            StdOut.println(arr[i]);
        }
        StdOut.println(" ");

        StdOut.println("findHorizontalSeam: ");
        int[] arr2 = t.findHorizontalSeam();
        for (int i = 0; i < arr2.length; i++) {
            StdOut.println(arr2[i]);
        }
        StdOut.println(" ");

        double[][] em = SCUtility.toEnergyMatrix(t);

        int rows = em.length;
        int cols = em[0].length;

        // Determine the maximum width of elements for formatting
        int maxWidth = 0;
        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) {
                String str = String.format("%.2f", em[i][j]);
                maxWidth = Math.max(maxWidth, str.length());
            }
        }
        
        StdOut.println("Original pixel matrix:");
        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) {
                String formattedValue = String.format("%.2f", em[i][j]);
                System.out.printf("%-" + (maxWidth + 2) + "s", formattedValue); // Adjust width for formatting
            }
            System.out.println(); // Move to the next row
        }
        StdOut.println(" ");

        

        t.removeVerticalSeam(t.findVerticalSeam());

        double[][] em2 = SCUtility.toEnergyMatrix(t);

        rows = em2.length;
        cols = em2[0].length;

        // Determine the maximum width of elements for formatting
        maxWidth = 0;
        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) {
                String str = String.format("%.2f", em2[i][j]);
                maxWidth = Math.max(maxWidth, str.length());
            }
        }
        
        StdOut.println("Pixel matrix after remove vertical seam:");
        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) {
                String formattedValue = String.format("%.2f", em2[i][j]);
                System.out.printf("%-" + (maxWidth + 2) + "s", formattedValue); // Adjust width for formatting
            }
            System.out.println(); // Move to the next row
        }

    }

}
