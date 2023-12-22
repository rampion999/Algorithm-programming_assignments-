
import java.util.LinkedList;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    private static final int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform() {
        String input = BinaryStdIn.readString();
        CircularSuffixArray t = new CircularSuffixArray(input);

        int first = -1;
        LinkedList<Character> q = new LinkedList<Character>();
        for (int i = 0; i < input.length(); i++) {
            if (t.index(i) == 0) {
                first = i;
                BinaryStdOut.write(first);
                for (char character : q) {
                    BinaryStdOut.write(character);
                }
                BinaryStdOut.write(input.charAt(input.length() - 1));
            } else if (first != -1) {
                BinaryStdOut.write(input.charAt(t.index(i) - 1));
            } else {
                q.add(input.charAt(t.index(i) - 1));
            }
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String input = BinaryStdIn.readString();

        int[] next = new int[input.length()];
        char[] sArr = new char[input.length()];
        for (int i = 0; i < input.length(); i++) {
            next[i] = i;
            sArr[i] = input.charAt(i);
        }

        lsdSort(sArr, next);

        int idx = first;
        BinaryStdOut.write(sArr[idx]);
        for (int i = 1; i < sArr.length; i++) {
            idx = next[idx];
            BinaryStdOut.write(sArr[idx]);         
        }

        BinaryStdOut.close();
    }

    private static void lsdSort(char[] s, int[] next) {
        char[] aux = new char[s.length];
        int[] count = new int[R + 1];
        for (int i = 0; i < s.length; i++) {
            count[s[i] + 1]++;
        }

        for (int r = 0; r < R; r++) {
            count[r + 1] += count[r];
        }

        int[] auxIndex = new int[s.length];
        for (int i = 0; i < s.length; i++) {
            auxIndex[count[s[i]]] = next[i];
            aux[count[s[i]]++] = s[i];
        }

        for (int i = 0; i < s.length; i++) {
            next[i] = auxIndex[i];
            s[i] = aux[i];
        }
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            BurrowsWheeler.transform();
        } else if (args[0].equals("+")) {
            BurrowsWheeler.inverseTransform();
        } else {
            throw new IllegalArgumentException();
        }       
    }
}