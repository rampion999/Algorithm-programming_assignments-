
import java.util.LinkedList;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class SAP {

    // All methods (and the constructor) should take time 
    // at most proportional to E + V in the worst case
    // space proportional to E + V

    private final Digraph g;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        g = new Digraph(G);
    }


    private class BreadthFirstPaths {
        private int dist = -1;
        private int ancestor = -1;

        // Two vertices BFS
        public BreadthFirstPaths(Digraph G, int v, int w) {
            boolean[] markedV = new boolean[G.V()];
            boolean[] markedW = new boolean[G.V()];
            LinkedList<Integer> qV = new LinkedList<Integer>();
            LinkedList<Integer> qW = new LinkedList<Integer>();
            
            markedV[v] = true;
            markedW[w] = true;

            qV.add(v);
            qW.add(w);

            bfs(G, markedV, markedW, qV, qW);
        }

        // Sets BFS
        public BreadthFirstPaths(Digraph G, Iterable<Integer> v, Iterable<Integer> w) {          
            boolean[] markedV = new boolean[G.V()];
            boolean[] markedW = new boolean[G.V()];
            LinkedList<Integer> qV = new LinkedList<Integer>();
            LinkedList<Integer> qW = new LinkedList<Integer>();
            for (Integer i : v) {
                if (i == null || i < 0 || i >= G.V()) {
                    throw new IllegalArgumentException();
                }

                if (!markedV[i]) {
                    qV.add(i);
                    markedV[i] = true;
                }             
            }
            for (Integer i : w) {
                if (i == null || i < 0 || i >= G.V()) {
                    throw new IllegalArgumentException();
                }

                if (markedV[i]) {
                    ancestor = i;
                    dist = 0;
                    return;
                }

                if (!markedW[i]) {
                    qW.add(i);
                    markedW[i] = true;
                }  
            }
            bfs(G, markedV, markedW, qV, qW);
        }

        private void bfs(Digraph G, boolean[] mkV, boolean[] mkW, LinkedList<Integer> qV, LinkedList<Integer> qW) {
            boolean[] marked;
            int[] distToV = new int[G.V()];
            int[] distToW = new int[G.V()];
            LinkedList<Integer> q;
            int[] distTo;
            int dut;

            boolean vTurn = true;
            int distTmp;
            while (!qV.isEmpty() || !qW.isEmpty()) {
                // Chose to do V or W.
                if (vTurn) {
                    q = qV;
                    distTo = distToV;
                    marked = mkV;
                } else {
                    q = qW;
                    distTo = distToW;
                    marked = mkW;
                }

                if (!q.isEmpty()) {
                    dut = q.remove();
                    for (int i : G.adj(dut)) {
                        if (!marked[i]) {
                            q.add(i);
                            marked[i] = true;
                            distTo[i] = distTo[dut] + 1;
                        }

                        // If find common vertex, Check it's path length
                        if (mkV[i] && mkW[i]) {
                            distTmp = distToV[i] + distToW[i];
                            if (dist == -1 || distTmp < dist) {
                                ancestor = i;
                                dist = distTmp;
                            }              
                        }
                    }
                }
                vTurn = !vTurn;
            }
        }

        public int getDist() {
            return dist;
        }

        public int getAncestor() {
            return ancestor;
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v >= this.g.V() || w >= this.g.V() || v < 0 || w < 0) {
            throw new IllegalArgumentException();
        }
        if (v == w) {
            return 0;
        }
        BreadthFirstPaths ans = new BreadthFirstPaths(this.g, v, w);
        return ans.getDist();
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v >= this.g.V() || w >= this.g.V() || v < 0 || w < 0) {
            throw new IllegalArgumentException();
        }
        if (v == w) {
            return v;
        }
        BreadthFirstPaths ans = new BreadthFirstPaths(this.g, v, w);
        return ans.getAncestor();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        BreadthFirstPaths ans = new BreadthFirstPaths(this.g, v, w);
        return ans.getDist();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        BreadthFirstPaths ans = new BreadthFirstPaths(this.g, v, w);
        return ans.getAncestor();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}