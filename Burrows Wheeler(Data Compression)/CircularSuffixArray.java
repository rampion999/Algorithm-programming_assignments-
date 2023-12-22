
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private static final int R = 256;

    private final int n;
    private final int[] index;


    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        n = s.length();
        index = new int[n];
        for (int i = 0; i < n; i++) {
            index[i] = i;
        }
        msdSort(s, index);
    }

    private static void msdSort(String dut, int[] index) {
        sort(dut, index, 0, dut.length() - 1, 0);
    }

    private static int charAt(String s, int d) {
        return s.charAt(d % s.length());
    }

    private static void sort(String dut, int[] index, int lo, int hi, int d) {
        if (hi <= lo || d == dut.length()) {
            return;
        }

        int[] count = new int[R + 2];
        for (int i = lo; i <= hi; i++) {
            count[charAt(dut, index[i] + d) + 2]++;
        }

        for (int r = 0; r < R + 1; r++) {
            count[r + 1] += count[r];
        }

        int[] indexAux = new int[hi - lo + 1];
        for (int i = lo; i <= hi; i++) {
            indexAux[count[charAt(dut, index[i] + d) + 1]++] = index[i]; 
        }

        
        for (int i = lo; i <= hi; i++) {
            index[i] = indexAux[i - lo];
        }

        for (int r = 0; r < R; r++) {
            sort(dut, index, lo + count[r], lo + count[r + 1] - 1, d + 1);
        }
    }

    // length of s
    public int length() {
        return n;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= n) {
            throw new IllegalArgumentException();
        }
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        // String s = "TYRIVSHMOTYRIVSHMOTYRIVSHMOTYRIVSHMO";
        CircularSuffixArray t = new CircularSuffixArray(s);
        for (int i = 0; i < t.length(); i++) {
            StdOut.println("index[" + i + "] = " + t.index(i));
        }
    }

}