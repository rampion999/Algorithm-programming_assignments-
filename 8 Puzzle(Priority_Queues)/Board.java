

import java.util.LinkedList;
import edu.princeton.cs.algs4.StdOut;


public class Board {

    private int n;
    private int[][] bd;
    private int blankTh = 0;
    private boolean goal = true;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        bd = new int[n][n];
        int target = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                bd[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    blankTh = i * n + j + 1;
                }
                if (tiles[i][j] != 0 && bd[i][j] != target) {
                    this.goal = false;
                }
                target += 1;
            }
        }
    }
                                           
    // string representation of this board
    public String toString() {
        String s = Integer.toString(n) + "\n";
        int desiredLength = Integer.toString(n * n - 1).length() + 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s += String.format("%-" + desiredLength + "s", Integer.toString(bd[i][j]));
            }
            s += "\n";
        }
        return s;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int c = 0;
        int target = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (bd[i][j] != target && target != n * n) {
                    c += 1;
                }
                target += 1;
            }
        }
        return c;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int mNum = 0;
        int target = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (bd[i][j] != target && bd[i][j] != 0) {
                    int[] posTar = pos(n, bd[i][j]);
                    mNum += Math.abs(i - posTar[0]) + Math.abs(j - posTar[1]);
                }
                target += 1;
            }
        }
        return mNum;
    }

    private static int[] pos(int n, int num) {
        int[] ans = new int[2];
        ans[0] = (num - 1) / n;
        ans[1] = (num - 1) % n;
        return ans;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.goal;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (y == null || !(y.getClass().equals(this.getClass())) || ((Board) y).dimension() != this.n) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.bd[i][j] != ((Board) y).bd[i][j]) {
                    return false;
                }
            }
        }  
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedList<Board> neighborLL = new LinkedList<Board>();
        int[] blankPos = pos(n, blankTh);
        int[] tarPos;
        if (blankPos[0] != 0) {
            tarPos = blankPos.clone();
            tarPos[0] -= 1;
            neighborLL.add(new Board(swap(this.bd, blankPos, tarPos)));
        }

        if (blankPos[0] != n - 1) {
            tarPos = blankPos.clone();
            tarPos[0] += 1;
            neighborLL.add(new Board(swap(this.bd, blankPos, tarPos)));
        }

        if (blankPos[1] != 0) {
            tarPos = blankPos.clone();
            tarPos[1] -= 1;
            neighborLL.add(new Board(swap(this.bd, blankPos, tarPos)));
        }

        if (blankPos[1] != n - 1) {
            tarPos = blankPos.clone();
            tarPos[1] += 1;
            neighborLL.add(new Board(swap(this.bd, blankPos, tarPos)));
        }

        return neighborLL;
    }

    private static int[][] swap(int[][] bd, int[] a, int[] b) {
        int n = bd.length;
        int[][] newBd = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newBd[i][j] = bd[i][j];
            }
        }
        newBd[a[0]][a[1]] = bd[b[0]][b[1]];
        newBd[b[0]][b[1]] = bd[a[0]][a[1]];
        return newBd;
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (blankTh != 1) {
            if (blankTh != 2) {
                return new Board(swap(this.bd, pos(n, 1), pos(n, 2)));
            } else {
                return new Board(swap(this.bd, pos(n, 1), pos(n, 1 + n)));
            }    
        } else {
            return new Board(swap(this.bd, pos(n, 2), pos(n, 2 + n)));
        }
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // int[][] test = { 
        //     { 8, 1, 3 }, 
        //     { 4, 0, 2 }, 
        //     { 7, 6, 5 } 
        // };

        int[][] test = { 
            { 1, 2, 3 }, 
            { 4, 5, 6 }, 
            { 7, 8, 0 } 
        };

        Board dut = new Board(test);

        StdOut.println("========================");
        StdOut.println("dimension:");
        StdOut.println(dut.dimension());

        StdOut.println("========================");
        StdOut.println("toString:");
        StdOut.println(dut.toString());

        StdOut.println("========================");
        StdOut.println("hamming:");
        StdOut.println(dut.hamming());

        StdOut.println("========================");
        StdOut.println("manhattan:");
        StdOut.println(dut.manhattan());

        StdOut.println("========================");
        StdOut.println("isGoal:");
        StdOut.println(dut.isGoal());

        StdOut.println("========================");
        StdOut.println("neighbors:");
        for (Board nbs : dut.neighbors()) {
            StdOut.println(nbs.toString());
        }

        StdOut.println("========================");
        StdOut.println("twin:");
        StdOut.println(dut.twin().toString());

    }
}