package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void testAddAndRemove(){
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

        assertEquals("removeFirst error!","i",deque.removeFirst());
        assertEquals("removeLast error!","you",deque.removeLast());
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

}
