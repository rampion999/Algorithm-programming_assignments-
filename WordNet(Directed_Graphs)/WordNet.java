
import java.util.HashMap;
import java.util.HashSet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

public class WordNet {

    // use space linear in the input size
    private final HashMap<String, HashSet<Integer>> wordIdSets;
    private final HashMap<Integer, String> idSynset;
    private final SAP sap;

    // constructor takes the name of the two input files
    // The constructor should take time linearithmic (or better) in the input size
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        
        // Create HashMap Key: word, Value: ID sets correspond to word.
        wordIdSets = new HashMap<String, HashSet<Integer>>();
        idSynset = new HashMap<Integer, String>();
        In in = new In(synsets);
        int cnt = 0;
        while (!in.isEmpty()) {
            String[] i = in.readLine().split(",");
            int id = Integer.parseInt(i[0]);
            String synset = i[1];
            String[] nouns = synset.split(" ");
            for (String s : nouns) {
                for (int j = 0; j < nouns.length; j++) {
                    if (!wordIdSets.containsKey(s)) {
                        wordIdSets.put(s, new HashSet<Integer>());
                    }
                    wordIdSets.get(s).add(id);                 
                }
            }
            idSynset.put(id, synset);
            cnt += 1;
        }

        // Create WordNet directed graph
        Digraph wordGrahp = new Digraph(cnt);
        in = new In(hypernyms);
        while (!in.isEmpty()) {
            String[] i = in.readLine().split(",");
            int v = Integer.parseInt(i[0]);
            for (int j = 1; j < i.length; j++) {
                int w = Integer.parseInt(i[j]);
                wordGrahp.addEdge(v, w);
            }
        }

        // Check if WordNet has only one root.
        boolean findRoot = false;
        for (int i = 0; i < wordGrahp.V(); i++) {
            if (wordGrahp.outdegree(i) == 0) {
                if (findRoot) {
                    throw new IllegalArgumentException();
                }
                findRoot = true;
            }
        }

        // Check if WordNet is cyclic.
        Topological dagCheck = new Topological(wordGrahp);
        if (!dagCheck.hasOrder()) {
            throw new IllegalArgumentException();
        }
        
        sap = new SAP(wordGrahp);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordIdSets.keySet();
    }

    // is the word a WordNet noun?
    // run in time logarithmic (or better) in the number of nouns
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return wordIdSets.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    // time linear in the size of the WordNet digraph
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }
        return sap.length(wordIdSets.get(nounA), wordIdSets.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    // time linear in the size of the WordNet digraph
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }
        return idSynset.get(sap.ancestor(wordIdSets.get(nounA), wordIdSets.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet t = new WordNet("synsets.txt", "hypernyms.txt");
        StdOut.println("Is 'ASCII_text_file' a word? " + t.isNoun("ASCII_text_file"));
        for (String string : t.nouns()) {
            StdOut.println(string);
        }
    }
}