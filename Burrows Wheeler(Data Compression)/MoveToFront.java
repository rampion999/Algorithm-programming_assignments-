
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to
    // standard output
    public static void encode() {
        char[] cArr = initAsciiArr();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();       
            moveToFrontEn(cArr, c);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] cArr = initAsciiArr();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            BinaryStdOut.write(cArr[c]);
            moveToFrontDe(cArr, c);
        }
        BinaryStdOut.close();
    }

    private static char[] initAsciiArr() {
        char[] cArr = new char[256];
        for (char i = 0; i < cArr.length; i++) {
            cArr[i] = i;
        }
        return cArr;
    }

    private static void moveToFrontEn(char[] arr, char dut) {
        char cur;
        char pre = arr[0];
        if (pre == dut) {
            BinaryStdOut.write((char) 0);
            return;
        }
        for (char i = 1; i <= arr.length; i++) {
            cur = arr[i];
            arr[i] = pre;
            if (cur == dut) {
                arr[0] = dut;
                BinaryStdOut.write(i);
                return;
            }
            pre = cur;
        }
    }

    private static void moveToFrontDe(char[] arr, char pos) {
        char val = arr[pos];
        char cur;
        char pre = arr[0];
        for (int i = 1; i <= pos; i++) {
            cur = arr[i];
            arr[i] = pre;
            pre = cur;
        }
        arr[0] = val;
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            MoveToFront.encode();
        } else if (args[0].equals("+")) {
            MoveToFront.decode();
        } else {
            throw new IllegalArgumentException();
        }       
    }
}