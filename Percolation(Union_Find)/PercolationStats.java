
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;


public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;

    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;
    private double[] thresholds;
    private int totalSides;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        } else {
            totalSides = n * n;
            thresholds = new double[trials];
            for (int i = 0; i < trials; i++) {
                int[][] duts = setsGenerate(n);
                Percolation dut = new Percolation(n);
                int openCounts = 0;
                while (!dut.percolates()) {
                    dut.open(duts[openCounts][0], duts[openCounts][1]);
                    openCounts += 1;
                }
                thresholds[i] = (double) openCounts / (totalSides);
            }
            mean = StdStats.mean(thresholds);
            stddev = StdStats.stddev(thresholds);
            confidenceLo = mean - (CONFIDENCE_95 * stddev / Math.sqrt(trials));
            confidenceHi = mean + (CONFIDENCE_95 * stddev / Math.sqrt(trials));
        }
    }

    private int[][] setsGenerate(int n) {
        int[][] tmp = new int[n * n][2];
        int cnt = 0;
        for (int c = 1; c < n + 1; c++) {
            for (int r = 1; r < n + 1; r++) {
                tmp[cnt][0] = c;
                tmp[cnt][1] = r;
                cnt += 1;
            }
        }
        StdRandom.shuffle(tmp);
        return tmp;
    }

    // sample mean of percolation threshold
    public double mean() {
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return this.stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.confidenceHi;
    }

   // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats test = new PercolationStats(n, t);
        StdOut.printf("mean                    = %f\n", test.mean());
        StdOut.printf("stddev                  = %f\n", test.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n", test.confidenceLo(), test.confidenceHi());
    }
}