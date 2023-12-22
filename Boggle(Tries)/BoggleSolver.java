
import java.util.HashSet;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {

    private final HashSet<String> validWords;
    private Node root;

    private class Node {
        public int score = -1;
        public Node[] next = new Node[26];

        public int get(String key) {
            Node x = get(root, key, 0);
            if (x == null) {
                return -1;
            }
            return  x.score;
        }

        private Node get(Node x, String key, int d) {
            if (x == null) {
                return null;
            }
            if (d == key.length()) {
                return x;
            }
            int c = key.charAt(d) - 'A';
            return get(x.next[c], key, d + 1);
        }
    }


    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null || dictionary.length == 0) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < dictionary.length; i++) {
            if (dictionary[i] == null) {
                throw new IllegalArgumentException();
            }
            root = put(root, dictionary[i], 0, 0);
        }
        validWords = new HashSet<String>();
    }

    private Node put(Node x, String key, int score, int d) {
        if (x == null) {
            x = new Node();
        }
        if (d == key.length()) {
            x.score = score;
            return x;
        }
        int c = key.charAt(d) - 'A';
        x.next[c] = put(x.next[c], key, score, d + 1);
        return x;
    }

    

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        validWords.clear();
        int row = board.rows();
        int col = board.cols();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {               
                boolean[][] marked = new boolean[row][col];
                search(root, board, "", marked, i, j, 0);
            }
        }
        return validWords;
    }

    private void search(Node node, BoggleBoard board, String pre, boolean[][] marked, int row, int col, int wordLen) {
        marked[row][col] = true;
        int idx = board.getLetter(row, col) - 'A';

        if (node.next[idx] == null) {
            marked[row][col] = false;
            return;
        }

        String newLet = String.valueOf(board.getLetter(row, col));

        Node nxtNode;
        if (board.getLetter(row, col) == 'Q') {
            nxtNode = node.next[idx].next['U' - 'A'];
            if (nxtNode == null) {
                marked[row][col] = false;
                return;
            }
            wordLen += 2;
            newLet = "QU";           
        } else {
            wordLen += 1;
            nxtNode = node.next[idx];
        }
        
        String dut = pre + newLet;

        if (wordLen > 2 && nxtNode.score == 0) {
            validWords.add(dut);
        }

        // check upper left
        if (row != 0 && col != 0 && !marked[row - 1][col - 1]) {
            search(nxtNode, board, dut, marked, row - 1, col - 1, wordLen);
        }

        // check upper mid
        if (row != 0 && !marked[row - 1][col]) {
            search(nxtNode, board, dut, marked, row - 1, col, wordLen);
        }

        // check upper right
        if (row != 0 && col != board.cols() - 1 && !marked[row - 1][col + 1]) {
            search(nxtNode, board, dut, marked, row - 1, col + 1, wordLen);
        }

        // check left
        if (col != 0 && !marked[row][col - 1]) {
            search(nxtNode, board, dut, marked, row, col - 1, wordLen);
        }

        // check right
        if (col != board.cols() - 1 && !marked[row][col + 1]) {
            search(nxtNode, board, dut, marked, row, col + 1, wordLen);
        }

        // check lower left
        if (row != board.rows() - 1 && col != 0 && !marked[row + 1][col - 1]) {
            search(nxtNode, board, dut, marked, row + 1, col - 1, wordLen);
        }

        // check lower mid
        if (row != board.rows() - 1 && !marked[row + 1][col]) {
            search(nxtNode, board, dut, marked, row + 1, col, wordLen);
        }

        // check lower right   
        if (row != board.rows() - 1 && col != board.cols() - 1 && !marked[row + 1][col + 1]) {         
            search(nxtNode, board, dut, marked, row + 1, col + 1, wordLen);
        }

        marked[row][col] = false;
    }

    private static int pScore(int wordLen) {
        if (wordLen >= 8) {
            return 11;
        } else if (wordLen == 7) {
            return 5;
        } else if (wordLen == 6) {
            return 3;
        } else if (wordLen == 5) {
            return 2;
        } else if (wordLen > 2) {
            return 1;
        } else {
            return 0;
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        if (validWords.contains(word) || root.get(word) != -1) {
            return pScore(word.length());
        }
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        int cnt = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
            cnt++;
        }
        StdOut.println("Score = " + score);
        StdOut.println("Valid word counts = " + cnt);
    }
}