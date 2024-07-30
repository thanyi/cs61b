package deque;

import afu.org.checkerframework.checker.igj.qual.I;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void testAddAndRemove(){
        ArrayDeque<Integer> deque = new ArrayDeque<>();

        deque.addLast(82);
        deque.addLast(82);
        deque.removeLast();


//        assertEquals("removeFirst error!","i",deque.removeFirst());
//        assertEquals("removeLast error!","you",deque.removeLast());
    }
    @Test
    public void testGet(){
        ArrayDeque<String> deque = new ArrayDeque<>();
        deque.addLast("i");
        deque.addLast("love");
        deque.addLast("you");
        deque.addLast("you");
        deque.addLast("you");
        deque.addLast("you");
        deque.addFirst("i");
        deque.addFirst("i");
        deque.addFirst("i");
        deque.addFirst("i");

        assertEquals("removeFirst error!","i",deque.get(0));

    }

    @Test
    public void testRandom(){
        ArrayDeque<Integer> deque = new ArrayDeque<>();

        int N = 100000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 5);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                deque.addLast(randVal);
                deque.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size1 = deque.size();
//                System.out.println("size: " + size1);

            }
            else if (operationNumber == 3) {
                // removelast

                deque.removeLast();
                System.out.println("removelast(" + ")");
            }
        }
    }
}
