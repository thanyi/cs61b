package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);

        IntList lst2 = IntList.of(4, 5, 6, 7, 8);
        boolean changed2 = IntListExercises.squarePrimes(lst2);
        assertEquals("4 -> 25 -> 6 -> 49 -> 8", lst2.toString());
        assertTrue(changed2);

        IntList lst3 = IntList.of(1, 2, 3, 4, 5);
        boolean changed3 = IntListExercises.squarePrimes(lst3);
        assertEquals("1 -> 4 -> 9 -> 4 -> 25", lst3.toString());
        assertTrue(changed3);

    }
}
