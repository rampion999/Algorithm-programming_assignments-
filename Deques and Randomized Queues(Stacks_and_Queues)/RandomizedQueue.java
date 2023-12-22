
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int totalCount = 0;
    private Item[] bag;


    // construct an empty randomized queue
    public RandomizedQueue() {
        bag = (Item []) new Object[2]; 
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return totalCount == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return totalCount;
    }

    private void resize(int capacity) {
        Item[] copy = (Item []) new Object[capacity];
        for (int i = 0; i < totalCount; i++) {
            copy[i] = bag[i];
        }
        bag = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (totalCount == bag.length) {
            resize(2 * bag.length);
        }
        bag[totalCount] = item;
        totalCount += 1;
    }

    
    // remove and return a random item
    public Item dequeue() {
        if (totalCount == 0) {
            throw new NoSuchElementException();
        }

        int targetPos = StdRandom.uniformInt(totalCount);
        Item q = bag[targetPos];

        totalCount -= 1;

        if (targetPos != totalCount) {
            bag[targetPos] = bag[totalCount];
        }
        bag[totalCount] = null;

        if (totalCount > 0 && totalCount == bag.length / 4) {
            resize(bag.length / 2);
        }

        return q;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (totalCount == 0) {
            throw new NoSuchElementException();
        }
        return bag[StdRandom.uniformInt(totalCount)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RdqIterator();
    }

    private class RdqIterator implements Iterator<Item> {
        private int[] posList;
        private int currentIdx = 0;

        RdqIterator() {
            posList = StdRandom.permutation(totalCount);
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            return currentIdx < totalCount;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item returnItem = bag[posList[currentIdx]];
            currentIdx += 1;
            return returnItem;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();

        StdOut.printf("Empty?                   = %b\n", q.isEmpty());

        q.enqueue("a");
        q.enqueue("b"); 
        q.enqueue("c"); 
        q.enqueue("d"); 
        q.enqueue("e");

        StdOut.printf("Empty?                   = %b\n", q.isEmpty());
        StdOut.printf("Size                     = %d\n", q.size());

        StdOut.printf("Sample                   = %s\n", q.sample());
        StdOut.printf("Sample                   = %s\n", q.sample());
        StdOut.printf("Sample                   = %s\n", q.sample());
        StdOut.printf("Sample                   = %s\n", q.sample());
        StdOut.printf("Sample                   = %s\n", q.sample());

        for (String i : q) {
            System.out.println(i);
        }

        for (String i : q) {
            System.out.println(i);
        }

        for (String i : q) {
            System.out.println(i);
        }

        StdOut.printf("Dequeue                  = %s\n", q.dequeue());
        StdOut.printf("Dequeue                  = %s\n", q.dequeue());
        StdOut.printf("Size                     = %d\n", q.size());
        StdOut.printf("Dequeue                  = %s\n", q.dequeue());
        StdOut.printf("Dequeue                  = %s\n", q.dequeue());
        StdOut.printf("Dequeue                  = %s\n", q.dequeue());

        StdOut.printf("Empty?                   = %b\n", q.isEmpty());
   
    }
}