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
    public void testaddsizeempty() {
        ArrayDeque<String> dq = new ArrayDeque<>();
        assertEquals(true, dq.isEmpty());

        dq.addFirst("first");
        assertEquals(1, dq.size());

        dq.addLast("last");
        assertEquals(2, dq.size());

        dq.printDeque();

        String first = dq.removeFirst();
        assertEquals("first", first);

        String last = dq.removeLast();
        assertEquals("last", last);

        assertEquals(0, dq.size());
    }

//    @Test
//    public void nonEmptyInstantiationTest() {
//        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>(1);
//
//        assertFalse("Should not be empty", arrayDeque.isEmpty());
//        assertEquals("Should have size 1", 1, arrayDeque.size());
//    }

    @Test
    public void addWithResizingTest() {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();

        for (int i = 0; i < 20; i++) {
            arrayDeque.addLast(i);
        }

        assertEquals("Should have size 20", 20, arrayDeque.size());

        for (int i = 0; i < 20; i++) {
            assertEquals("Should the same", (int)i, (int)arrayDeque.get(i));
        }



    }

    @Test
    public void addWithResizingTest2() {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();

        for (int i = 0; i < 10000; i++) {
            arrayDeque.addLast(i);
        }

        for (int i = 0; i <9999; i++) {
            arrayDeque.removeLast();
        }
    }

    @Test
    public void addBigAmountTest() {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();

        int M = 1000000;

        for (int i = 0; i < M; i++) {
            arrayDeque.addLast(i);
        }

        assertEquals("Should have size 1000000", M, arrayDeque.size());
    }

    @Test
    public void removeTest() {
        ArrayDeque<String> arrayDeque = new ArrayDeque<>();

        arrayDeque.addFirst("front");
        arrayDeque.addLast("middle");
        arrayDeque.addLast("back");

        assertEquals("Should remove last item", "back", arrayDeque.removeLast());
        assertEquals("Should remove first item", "front", arrayDeque.removeFirst());

        assertEquals("Should have size 1", 1, arrayDeque.size());
    }


    @Test
    public void removeWithResizingTest() {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();

        for (int i = 0; i < 20; i++) {
            arrayDeque.addLast(i);
        }

        for (int i = 0; i < 20; i++) {
            assertEquals("Should be equal", i, (int) arrayDeque.removeFirst());
        }

        assertTrue("Should be empty", arrayDeque.isEmpty());

        for (int i = 0; i < 20; i++) {
            arrayDeque.addLast(i);
        }

        assertEquals("Should have size 20", 20, arrayDeque.size());
    }

    @Test
    public void removeBigAmountTest() {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();

        int M = 1000000;

        for (int i = 0; i < M; i++) {
            arrayDeque.addLast(i);
        }

        assertEquals("Should have size 1000000", M, arrayDeque.size());

        for (int i = 0; i < M; i++) {
            assertEquals("Should be equal", i, (int) arrayDeque.removeFirst());
        }

        assertTrue("Should be empty", arrayDeque.isEmpty());
    }

    @Test
    public void getTest() {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();

        for (int i = 0; i < 20; i++) {
            arrayDeque.addLast(i);
        }

        for (int i = 0; i < 20; i++) {
            assertEquals("Should be equal", i, (int) arrayDeque.get(i));
        }

        assertNull("Should be null when index out of bound", arrayDeque.get(20));
    }

    @Test
    public void getBigAmountTest() {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();

        int M = 1000000;

        for (int i = 0; i < M; i++) {
            arrayDeque.addLast(i);
        }

        for (int i = 0; i < M; i++) {
            assertEquals("Should be equal", i, (int) arrayDeque.get(i));
        }
    }




    @Test
    public void randomizedTest() {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();

        int N = 1000000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                int randVal = StdRandom.uniform(0, 100);
                arrayDeque.addFirst(randVal);
            } else if (operationNumber == 1) {
                int randVal = StdRandom.uniform(0, 100);
                arrayDeque.addLast(randVal);
            } else if (arrayDeque.size() == 0) {
                assertTrue(arrayDeque.isEmpty());
            } else if (operationNumber == 2) {
                assertTrue(arrayDeque.size() > 0);
            } else if (operationNumber == 3) {
                arrayDeque.removeFirst();
            } else if (operationNumber == 4) {
                arrayDeque.removeLast();
            } else if (operationNumber == 5) {
                int randIndex = StdRandom.uniform(0, arrayDeque.size());
                arrayDeque.get(randIndex);
            }
        }
    }


    @Test
    /* Tests removing from an empty deque */
    public void testIterator() {
        LinkedListDeque<String> wordList = new LinkedListDeque<String>();

        wordList.addLast("I");
        wordList.addLast("love");
        wordList.addLast("you");
//        Iterator<String> LinkedListIterator = wordList.iterator();
        for(String s:wordList){
            System.out.println(s);
        }


//        assertEquals("Should have the same value", "love", item);
    }


    @Test
    /* Tests removing from an empty deque */
    public void testEqualsAndIterator() {
        ArrayDeque<String> wordList = new ArrayDeque<>();
        ArrayDeque<String> wordList2 = new ArrayDeque<>();

        wordList.addLast("I");
        wordList.addLast("love");
        wordList.addLast("you");

        wordList2.addLast("I");
        wordList2.addLast("love");
        wordList2.addLast("you");
//        System.out.println(wordList.equals(wordList2));
        for (String s : wordList){
            System.out.println(s);
        }

//        assertEquals("Should have the same value", true, item);
        assertTrue("Should have the same value", wordList.equals(wordList2));
    }
}
