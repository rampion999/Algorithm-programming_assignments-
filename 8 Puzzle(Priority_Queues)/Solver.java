

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import java.util.LinkedList;

public class Solver {
    
    private int minMove = -1;
    private SearchNode dut;
    private boolean solvable = true;

    private class SearchNode implements Comparable<SearchNode> {
       
        private Board bd;
        private int priority;
        private int moves = 0;
        private int manhattan;
        private SearchNode prev;
        private boolean goal;

        public SearchNode(SearchNode prevSN, Board inputBd) {
            this.prev = prevSN;
            this.bd = inputBd;
            if (prevSN != null) {
                this.moves = prevSN.moves + 1;
            }        
            this.manhattan = inputBd.manhattan();
            this.priority = this.moves + this.manhattan;
            this.goal = inputBd.isGoal();
        }


        public int compareTo(SearchNode that) {
            if (this.priority == that.priority) {
                return 0;
            } else if (this.priority < that.priority) {
                return -1;
            } else {
                return 1;
            }
        }

        public boolean isGoal() {
            return this.goal;
        }

        public Board getBoard() {
            return this.bd;
        }

        public SearchNode getPrev() {
            return this.prev;
        }

        public int getMove() {
            return this.moves;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        dut = new SearchNode(null, initial);
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        pq.insert(dut);

        Board twin = initial.twin();
        SearchNode dutTwin = new SearchNode(null, twin);
        MinPQ<SearchNode> pqTwin = new MinPQ<SearchNode>();
        pqTwin.insert(dutTwin);

        while (pq.size() != 0 && pqTwin.size() != 0) {
            dut = pq.delMin();
            if (dut.isGoal()) {
                break;
            }
            for (Board nxtBd : dut.getBoard().neighbors()) {
                if (dut.moves != 0 && (nxtBd.equals(dut.getPrev().getBoard()))) {
                    continue;
                }
                pq.insert(new SearchNode(dut, nxtBd));
            }

            dutTwin = pqTwin.delMin();
            if (dutTwin.isGoal()) {
                solvable = false;
                break;
            }   
            for (Board nxtBdTwin : dutTwin.getBoard().neighbors()) {
                if (dutTwin.moves != 0 && (nxtBdTwin.equals(dutTwin.getPrev().getBoard()))) {
                    continue;
                }
                pqTwin.insert(new SearchNode(dutTwin, nxtBdTwin));
            }
        }
        if (solvable) {
            minMove = dut.moves;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return this.minMove;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (this.solvable) {
            LinkedList<Board> collect = new LinkedList<Board>();
            SearchNode curr = dut;
            while (curr != null) {
                collect.addFirst(curr.getBoard());
                curr = curr.getPrev();
            }
            return collect;
        } else {
            return null;
        }
    }

    // test client (see below) 
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}