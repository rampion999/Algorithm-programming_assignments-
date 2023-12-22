
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int openedCount = 0;
    private WeightedQuickUnionUF u, d;
    private boolean[][] sites;
    private int n;
    private boolean isPerco = false;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int gridLen) {
        if (gridLen <= 0) {
            throw new IllegalArgumentException();
        } else {
            n = gridLen;
            u = new WeightedQuickUnionUF((n * n) + 1);
            d = new WeightedQuickUnionUF((n * n) + 1);
            sites = new boolean[n][n];
        }
    }

    private int indexCal(int row, int col) {
        return (row - 1) * n + col;
    }


    private void unionOperation(int row, int col) {
        int index = indexCal(row, col);

        // up
        if (row == 1) {
            u.union(0, index);
        } else if (isOpen(row - 1, col)) {
            u.union(index - n, index);
            d.union(index - n, index);
        }

        // down
        if (row == n) {
            d.union(0, index);
        } else if (isOpen(row + 1, col)) {
            u.union(index + n, index);
            d.union(index + n, index);
        }

        // left
        if (col != 1 && isOpen(row, col - 1)) {
            u.union(index - 1, index);
            d.union(index - 1, index);
        }

        // right
        if (col != n && isOpen(row, col + 1)) {
            u.union(index + 1, index);
            d.union(index + 1, index);
        }

        if (isFull(row, col) && isDownFull(row, col)) {
            isPerco = true;
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row > n || row <= 0 || col > n || col <= 0) {
            throw new IllegalArgumentException();
        } else {
            if (!isOpen(row, col)) {
                openSite(row, col);
                unionOperation(row, col);
                openedCount += 1;
            }
        }
    }

    // is the site (row, col) open?
    private void openSite(int row, int col) {
        sites[row - 1][col - 1] = true;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > n || row <= 0 || col > n || col <= 0) {
            throw new IllegalArgumentException();
        } else {
            return sites[row - 1][col - 1];
        }
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > n || row <= 0 || col > n || col <= 0) {
            throw new IllegalArgumentException();
        } else {
            if (!isOpen(row, col)) {
                return false;
            }
            int target = u.find(indexCal(row, col));
            int top = u.find(0);
            return target == top;
        }
    }

    private boolean isDownFull(int row, int col) {
        if (!isOpen(row, col)) {
            return false;
        }
        int target = d.find(indexCal(row, col));
        int down = d.find(0);
        return target == down;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.openedCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return isPerco;
    }

    // test client (optional)
    public static void main(String[] args) { }
}