
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private class IntNode {
        private IntNode prev;
        private Item item;
        private IntNode next;

        IntNode(IntNode p, Item i, IntNode n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    private IntNode sentinel;
    private int size;


    // construct an empty deque
    public Deque() {
        sentinel = new IntNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        size += 1;
        sentinel.next = new IntNode(sentinel, item, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        size += 1;
        sentinel.prev.next = new IntNode(sentinel.prev, item, sentinel);
        sentinel.prev = sentinel.prev.next;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        size -= 1;    
        IntNode qq = sentinel.next;
        Item moveItem = qq.item;
        sentinel.next = qq.next;
        sentinel.next.prev = sentinel;
        return moveItem;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        size -= 1;    
        IntNode qq = sentinel.prev;
        Item moveItem = qq.item;
        sentinel.prev = qq.prev;
        sentinel.prev.next = sentinel;
        return moveItem;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<Item> {
        private int wizPos;
        private IntNode wizNode;
        
        LinkedListDequeIterator() {
            wizPos = 0;
            wizNode = sentinel.next;
        }

        public boolean hasNext() {
            return wizPos < size;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item returnItem = wizNode.item;
            wizPos += 1;
            wizNode = wizNode.next;
            return returnItem;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> dut = new Deque<Integer>();

        StdOut.printf("Empty?                   = %b\n", dut.isEmpty());

        dut.addFirst(3);
        dut.addFirst(2);
        dut.addFirst(1);

        dut.addLast(4);
        dut.addLast(5);
        dut.addLast(6);

        for (int i : dut) {
            System.out.println(i);
        }

        StdOut.printf("Size                    = %d\n", dut.size());
        StdOut.printf("Empty?                  = %b\n", dut.isEmpty());

        StdOut.printf("Remove first            = %d\n", dut.removeFirst());
        StdOut.printf("Remove last             = %d\n", dut.removeLast());
    }

}